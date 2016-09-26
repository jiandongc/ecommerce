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

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/anoncarts")
public class AnonCartController {

    private final AnonCartService anonCartService;
    private final CartSummaryDataMapper mapper;

    @Autowired
    public AnonCartController(AnonCartService anonCartService, CartSummaryDataMapper mapper) {
        this.anonCartService = anonCartService;
        this.mapper = mapper;
    }

    @RequestMapping(method = POST)
    public ResponseEntity save(@RequestBody AnonCartItemData anonCartItemData) {
        final AnonCart anonCart = anonCartService.addItem(anonCartItemData);
        return createCartSummaryResponse(Optional.ofNullable(anonCart));
    }

    @RequestMapping(value = "/{cartUid}", method = PUT)
    public ResponseEntity updateCartCustomerId(@PathVariable UUID cartUid, @RequestBody Long customerId) {
        final Optional<AnonCart> anonCart = anonCartService.updateCartWithCustomerId(cartUid, customerId);
        if (anonCart.isPresent()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{cartUid}/items", method = PATCH)
    public ResponseEntity updateCartItem(@PathVariable("cartUid") UUID cartUid,
                                         @RequestParam("productId") Long productId,
                                         @RequestBody AnonCartItemData anonCartItemData) {
        final Optional<AnonCart> anonCart = anonCartService.updateCartItemWithProductId(cartUid, productId, anonCartItemData);
        if (anonCart.isPresent()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/summary/{cartUid}", method = GET)
    public ResponseEntity<CartSummaryData> getCartSummary(@PathVariable UUID cartUid) {
        final Optional<AnonCart> anonCart = anonCartService.findAnonCartByUid(cartUid);
        return createCartSummaryResponse(anonCart);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResponseEntity<CartSummaryData> getCartSummaryByCustomerId(@RequestParam("customerId") Long customerId) {
        final Optional<AnonCart> anonCart = anonCartService.findAnonCartByCustomerId(customerId);
        return createCartSummaryResponse(anonCart);
    }

    @RequestMapping(value = "/{cartUid}/cartItems/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCartItem(@PathVariable UUID cartUid, @PathVariable("productId") Long productId) {
        try {
            anonCartService.deleteCartItemByProductId(cartUid, productId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<CartSummaryData> createCartSummaryResponse(Optional<AnonCart> anonCart) {
        if (anonCart.isPresent()) {
            final CartSummaryData cartSummaryData = mapper.getValue(anonCart.get());
            return new ResponseEntity<CartSummaryData>(cartSummaryData, HttpStatus.OK);
        } else {
            return new ResponseEntity<CartSummaryData>(HttpStatus.NOT_FOUND);
        }
    }

}
