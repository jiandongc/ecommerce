package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartSummary;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.mapper.CartSummaryMapper;
import shoppingcart.service.ShoppingCartItemService;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/carts")
public class ShoppingCartItemController {

    private final ShoppingCartItemService cartItemService;
    private final CartSummaryMapper cartSummaryMapper;

    @Autowired
    public ShoppingCartItemController(ShoppingCartItemService cartItemService, CartSummaryMapper cartSummaryMapper) {
        this.cartItemService = cartItemService;
        this.cartSummaryMapper = cartSummaryMapper;
    }

    @RequestMapping(value = "{cartUid}/items", method = POST)
    public ResponseEntity<CartSummary> createCartItem(@PathVariable UUID cartUid,
                                                      @RequestBody ShoppingCartItem cartItem){
        try{
            final ShoppingCart cart = cartItemService.createCartItem(cartUid, cartItem);
            return new ResponseEntity<CartSummary>(cartSummaryMapper.map(cart), CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<CartSummary>(CONFLICT);
        }
    }

    @RequestMapping(value = "{cartUid}/items", method = DELETE)
    public ResponseEntity<CartSummary> deleteCartItem(@PathVariable UUID cartUid, @RequestBody String sku){
        final ShoppingCart cart = cartItemService.deleteCartItem(cartUid, sku);
        return new ResponseEntity<CartSummary>(cartSummaryMapper.map(cart), OK);
    }

//    @RequestMapping(value = "{cartUid}/items", method = PATCH)
//    public ResponseEntity<ShoppingCart> updateCartItem(@PathVariable UUID cartUid,
//                                                       @RequestBody ShoppingCartItem cartItem){
//        try{
//            final ShoppingCart cart = cartItemService.createCartItem(cartUid, cartItem);
//            return new ResponseEntity<ShoppingCart>(cart, CREATED);
//        } catch (Exception exception) {
//            return new ResponseEntity<ShoppingCart>(CONFLICT);
//        }
//    }
}
