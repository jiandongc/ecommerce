package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartSummary;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.CartSummaryMapper;
import shoppingcart.service.ShoppingCartService;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CartSummaryMapper cartSummaryMapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, CartSummaryMapper cartSummaryMapper) {
        this.shoppingCartService = shoppingCartService;
        this.cartSummaryMapper = cartSummaryMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createShoppingCart(@RequestBody(required = false) Long customerId){
        if(customerId != null){
            return new ResponseEntity<String>(shoppingCartService.createShoppingCartForUser(customerId).toString(), CREATED);
        } else {
            return new ResponseEntity<String>(shoppingCartService.createShoppingCartForGuest().toString(), CREATED);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = GET)
    public ResponseEntity<CartSummary> getShoppingCartByUid(@PathVariable UUID cartUid){
        final Optional<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCartByUid(cartUid);
        return shoppingCart.map(cart -> new ResponseEntity<>(cartSummaryMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET)
    public ResponseEntity<CartSummary> getShoppingCartByCustomerId(@RequestParam(value = "customerId") Long customerId){
        final Optional<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCartByCustomerId(customerId);
        return shoppingCart.map(cart -> new ResponseEntity<>(cartSummaryMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = PUT)
    public ResponseEntity updateCartCustomerId(@PathVariable UUID cartUid, @RequestBody Long customerId) {
        final Optional<ShoppingCart> shoppingCart = shoppingCartService.updateCustomerId(cartUid, customerId);
        return shoppingCart.map(cart -> new ResponseEntity(HttpStatus.OK))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}
