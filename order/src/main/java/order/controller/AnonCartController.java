package order.controller;

import order.data.AnonCartItemData;
import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.service.AnonCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/anoncarts")
public class AnonCartController {

    private AnonCartService anonCartService;

    @Autowired
    public AnonCartController(AnonCartService anonCartService){
        this.anonCartService = anonCartService;
    }

    @RequestMapping(method= POST)
    public CartSummaryData save(@RequestBody AnonCartItemData anonCartItemData){
        if(anonCartItemData.getCartUid() == null) {
            final AnonCart anonCart = anonCartService.addFirstItem(anonCartItemData);
            return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
        }
        else {
            final AnonCart anonCart = anonCartService.addAnotherItem(anonCartItemData);
            return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
        }
    }

    @RequestMapping(method= GET)
    public CartSummaryData findByCartUid(@RequestParam("cartuid") UUID cartUid) {
        AnonCart anonCart = anonCartService.findAnonCartByUid(cartUid);
        return new CartSummaryData(anonCart.getCartUid(), anonCart.getTotalCount(), anonCart.getTotalPrice());
    }

}
