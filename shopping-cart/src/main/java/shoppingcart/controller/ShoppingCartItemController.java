package shoppingcart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartData;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.mapper.CartDataMapper;
import shoppingcart.service.ShoppingCartItemService;
import shoppingcart.service.ShoppingCartService;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RestController
@RequestMapping("/carts")
public class ShoppingCartItemController {

    private final ShoppingCartItemService cartItemService;
    private final ShoppingCartService cartService;
    private final CartDataMapper cartDataMapper;

    @Autowired
    public ShoppingCartItemController(ShoppingCartItemService cartItemService, ShoppingCartService cartService, CartDataMapper cartDataMapper) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.cartDataMapper = cartDataMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items", method = POST)
    public ResponseEntity<CartData> createCartItem(@PathVariable UUID cartUid, @RequestBody ShoppingCartItem cartItem) {
        try {
            cartItemService.createCartItem(cartUid, cartItem);
            final Optional<ShoppingCart> cartOptional = cartService.getShoppingCartByUid(cartUid);
            return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), CREATED)).orElse(new ResponseEntity<>(NOT_FOUND));
        } catch (Exception exception) {
            log.error("Error creating shopping car item {}", exception);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items/{sku}", method = DELETE)
    public ResponseEntity<CartData> deleteCartItem(@PathVariable UUID cartUid, @PathVariable String sku) {
        try {
            cartItemService.deleteCartItem(cartUid, sku);
            final Optional<ShoppingCart> cartOptional = cartService.getShoppingCartByUid(cartUid);
            return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK)).orElse(new ResponseEntity<>(NOT_FOUND));
        } catch (Exception exception) {
            log.error("Error deleting shopping car item {}", exception);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/items", method = PUT)
    public ResponseEntity<CartData> updateCartItem(@PathVariable UUID cartUid, @RequestBody ShoppingCartItem cartItem) {
        try {
            cartItemService.updateQuantity(cartUid, cartItem.getSku(), cartItem.getQuantity());
            final Optional<ShoppingCart> cartOptional = cartService.getShoppingCartByUid(cartUid);
            return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK)).orElse(new ResponseEntity<>(NOT_FOUND));
        } catch (Exception exception) {
            log.error("Error updating shopping car item {}", exception);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
