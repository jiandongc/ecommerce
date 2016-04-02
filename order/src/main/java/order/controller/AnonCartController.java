package order.controller;

import order.data.AnonCartItemData;
import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.mapper.CartSummaryDataMapper;
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

    private final AnonCartService anonCartService;
    private final CartSummaryDataMapper mapper;

    @Autowired
    public AnonCartController(AnonCartService anonCartService, CartSummaryDataMapper mapper){
        this.anonCartService = anonCartService;
        this.mapper = mapper;
    }

    @RequestMapping(method = POST)
    public CartSummaryData save(@RequestBody AnonCartItemData anonCartItemData){
        if(anonCartItemData.getCartUid() == null) {
            final AnonCart anonCart = anonCartService.addFirstItem(anonCartItemData);
            return mapper.getValue(anonCart);
        } else {
            final AnonCart anonCart = anonCartService.addAnotherItem(anonCartItemData);
            return mapper.getValue(anonCart);
        }
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

    @RequestMapping(value = "/summary/{cartUid}", method = GET)
    public ResponseEntity<CartSummaryData> getCartSummary(@PathVariable UUID cartUid) {
        final AnonCart anonCart = anonCartService.findAnonCartByUid(cartUid);
        return createCartSummaryResponse(anonCart);
    }

    @RequestMapping(value = "/summary", method=RequestMethod.GET)
    public ResponseEntity<CartSummaryData> getCartSummaryByCustomerId(@RequestParam("customerId") Long customerId){
        final AnonCart anonCart = anonCartService.findAnonCartByCustomerId(customerId);
        return createCartSummaryResponse(anonCart);
    }

    private ResponseEntity<CartSummaryData> createCartSummaryResponse(AnonCart anonCart){
        if (anonCart != null){
            final CartSummaryData cartSummaryData = mapper.getValue(anonCart);
            return new ResponseEntity<CartSummaryData>(cartSummaryData, HttpStatus.OK);
        } else {
            return new ResponseEntity<CartSummaryData>(HttpStatus.NOT_FOUND);
        }
    }

}
