package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartData;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.mapper.CartDataMapper;
import shoppingcart.service.ShoppingCartItemService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/carts")
public class ShoppingCartItemController {

    private final ShoppingCartItemService cartItemService;
    private final CartDataMapper cartDataMapper;

    @Autowired
    public ShoppingCartItemController(ShoppingCartItemService cartItemService, CartDataMapper cartDataMapper) {
        this.cartItemService = cartItemService;
        this.cartDataMapper = cartDataMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items", method = POST)
    public ResponseEntity<CartData> createCartItem(@PathVariable UUID cartUid, @RequestBody ShoppingCartItem cartItem){
        try{
            final ShoppingCart cart = cartItemService.createCartItem(cartUid, cartItem);
            return new ResponseEntity<>(cartDataMapper.map(cart), CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items/{sku}", method = DELETE)
    public ResponseEntity<CartData> deleteCartItem(@PathVariable UUID cartUid, @PathVariable String sku){
        final ShoppingCart cart = cartItemService.deleteCartItem(cartUid, sku);
        return new ResponseEntity<>(cartDataMapper.map(cart), OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items", method = PUT)
    public ResponseEntity updateCartItem(@PathVariable UUID cartUid, @RequestBody ShoppingCartItem cartItem) {
        try{
            cartItemService.updateQuantity(cartUid, cartItem.getSku(), cartItem.getQuantity());
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception exception){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
