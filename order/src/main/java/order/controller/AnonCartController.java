package order.controller;

import order.data.AnonCartItemData;
import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.service.AnonCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/anoncarts")
public class AnonCartController {

    private AnonCartService anonCartService;

    @Autowired
    public AnonCartController(AnonCartService anonCartService){
        this.anonCartService = anonCartService;
    }

    @RequestMapping(method = POST)
    public CartSummaryData save(@RequestBody AnonCartItemData anonCartItemData){
        if(anonCartItemData.getCartUid() == null) {
            final AnonCart anonCart = anonCartService.addFirstItem(anonCartItemData);
            return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
        } else {
            final AnonCart anonCart = anonCartService.addAnotherItem(anonCartItemData);
            return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
        }
    }

    @RequestMapping(value = "/summary/{cartUid}", method = GET)
    public CartSummaryData getCartSummary(@PathVariable UUID cartUid) {
        final AnonCart anonCart = anonCartService.findAnonCartByUid(cartUid);
        return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
    }

    @RequestMapping(value = "/{cartUid}", method = PUT)
    public ResponseEntity updateCartCustomerId(@PathVariable UUID cartUid, @RequestBody Long customerId){
        final AnonCart anonCart = anonCartService.updateCustomerId(cartUid, customerId);
        if (anonCart != null) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
