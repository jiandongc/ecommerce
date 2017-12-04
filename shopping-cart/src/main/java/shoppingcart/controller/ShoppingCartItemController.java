package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.service.ShoppingCartItemService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/carts")
public class ShoppingCartItemController {

    private final ShoppingCartItemService cartItemService;

    @Autowired
    public ShoppingCartItemController(ShoppingCartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @RequestMapping(value = "{cartUid}/items", method = POST)
    public ResponseEntity<ShoppingCart> createCartItem(@PathVariable UUID cartUid,
                                                       @RequestBody ShoppingCartItem cartItem){
        try{
            final ShoppingCart cart = cartItemService.createCartItem(cartUid, cartItem);
            return new ResponseEntity<ShoppingCart>(cart, CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<ShoppingCart>(CONFLICT);
        }
    }

    @RequestMapping(value = "{cartUid}/items", method = PATCH)
    public ResponseEntity<ShoppingCart> updateCartItem(@PathVariable UUID cartUid,
                                                       @RequestBody ShoppingCartItem cartItem){
        try{
            final ShoppingCart cart = cartItemService.createCartItem(cartUid, cartItem);
            return new ResponseEntity<ShoppingCart>(cart, CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<ShoppingCart>(CONFLICT);
        }
    }
}
