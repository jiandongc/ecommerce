package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.service.ShoppingCartService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<String> createShoppingCart(@RequestBody(required = false) Long customerId){
        if(customerId != null){
            return new ResponseEntity<String>(shoppingCartService.createShoppingCartForUser(customerId).toString(), CREATED);
        } else {
            return new ResponseEntity<String>(shoppingCartService.createShoppingCartForGuest().toString(), CREATED);
        }
    }
}
