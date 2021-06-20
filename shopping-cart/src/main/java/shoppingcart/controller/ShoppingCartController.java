package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartData;
import shoppingcart.data.CustomerData;
import shoppingcart.data.DeliveryOptionData;
import shoppingcart.domain.*;
import shoppingcart.mapper.CartDataMapper;
import shoppingcart.service.DeliveryOptionService;
import shoppingcart.service.ShoppingCartService;
import shoppingcart.service.VoucherValidationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CartDataMapper cartDataMapper;
    private final VoucherValidationService voucherValidationService;
    private final DeliveryOptionService deliveryOptionService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  CartDataMapper cartDataMapper,
                                  VoucherValidationService voucherValidationService,
                                  DeliveryOptionService deliveryOptionService) {
        this.shoppingCartService = shoppingCartService;
        this.cartDataMapper = cartDataMapper;
        this.voucherValidationService = voucherValidationService;
        this.deliveryOptionService = deliveryOptionService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createShoppingCart(@RequestBody(required = false) CustomerData customerData) {
        if (customerData != null) {
            return new ResponseEntity<>(shoppingCartService.createShoppingCartForUser(customerData).toString(), CREATED);
        } else {
            return new ResponseEntity<>(shoppingCartService.createShoppingCartForGuest().toString(), CREATED);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = GET)
    public ResponseEntity<CartData> getShoppingCartByUid(@PathVariable UUID cartUid) {
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET)
    public ResponseEntity<CartData> getShoppingCartByCustomerUid(@RequestParam(value = "customerId") String customerId) {
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByCustomerUid(customerId);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = PUT)
    public ResponseEntity<CartData> addCustomerInfo(@PathVariable UUID cartUid, @RequestBody CustomerData customerData) {
        shoppingCartService.addCustomerInfo(cartUid, customerData);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/addresses", method = POST)
    public ResponseEntity<CartData> addAddress(@PathVariable UUID cartUid, @RequestBody Address address) {
        shoppingCartService.addAddress(cartUid, address);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/deliveryoption", method = POST)
    public ResponseEntity<CartData> addDeliveryOption(@PathVariable UUID cartUid, @RequestBody DeliveryOption deliveryOption) {
        shoppingCartService.addDeliveryOption(cartUid, deliveryOption);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/deliveryoption", method = GET)
    public List<DeliveryOptionData> getDeliveryOptions(@PathVariable UUID cartUid) {
        List<DeliveryOptionOffer> deliveryOptionOffers = deliveryOptionService.getDeliveryOptionOffers(cartUid, "UK");
        return deliveryOptionOffers.stream().map(cartDataMapper::map).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/promotion", method = POST)
    public ResponseEntity addPromotion(@PathVariable UUID cartUid, @RequestBody String voucherCode) {
        ValidationResult validationResult = voucherValidationService.validate(cartUid, voucherCode);
        if (validationResult.isValid()) {
            shoppingCartService.addPromotion(cartUid, voucherCode);
            final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
            return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                    .orElse(new ResponseEntity<>(NOT_FOUND));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}/promotion", method = DELETE)
    public ResponseEntity deletePromotion(@PathVariable UUID cartUid) {
        shoppingCartService.deletePromotion(cartUid);
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(cartDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

}
