package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartData;
import shoppingcart.domain.Address;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.CartDataMapper;
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
    private final CartDataMapper cartDataMapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, CartDataMapper cartDataMapper) {
        this.shoppingCartService = shoppingCartService;
        this.cartDataMapper = cartDataMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createShoppingCart(@RequestBody(required = false) Long customerId){
        if(customerId != null){
            return new ResponseEntity<>(shoppingCartService.createShoppingCartForUser(customerId).toString(), CREATED);
        } else {
            return new ResponseEntity<>(shoppingCartService.createShoppingCartForGuest().toString(), CREATED);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = GET)
    public ResponseEntity<CartData> getShoppingCartByUid(@PathVariable UUID cartUid){
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET)
    public ResponseEntity<CartData> getShoppingCartByCustomerId(@RequestParam(value = "customerId") Long customerId){
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByCustomerId(customerId);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = PUT)
    public ResponseEntity<CartData> updateCartCustomerId(@PathVariable UUID cartUid, @RequestBody Long customerId) {
        shoppingCartService.updateCustomerId(cartUid, customerId);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "{cartUid}/address", method = POST)
    public ResponseEntity<CartData> addAddress(@PathVariable UUID cartUid, @RequestBody Address address){
        shoppingCartService.addAddress(cartUid, address);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
