package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingcart.data.CartData;
import shoppingcart.data.CustomerData;
import shoppingcart.data.DeliveryOptionData;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.CartDataMapper;
import shoppingcart.service.ShoppingCartService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CartDataMapper cartDataMapper;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, CartDataMapper cartDataMapper) {
        this.shoppingCartService = shoppingCartService;
        this.cartDataMapper = cartDataMapper;
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
    public ResponseEntity<CartData> getShoppingCartByCustomerId(@RequestParam(value = "customerId") Long customerId) {
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByCustomerId(customerId);
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
        Optional<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCartByUid(cartUid);
        BigDecimal itemSubTotal = shoppingCart.map(ShoppingCart::getItemSubTotal).orElse(BigDecimal.ZERO);
        if (itemSubTotal.compareTo(BigDecimal.valueOf(40L)) < 0) {
            return Arrays.asList(
                    cartDataMapper.map(DeliveryOption.builder().method("Standard Delivery").charge(3D).minDaysRequired(3).maxDaysRequired(5).build()),
                    cartDataMapper.map(DeliveryOption.builder().method("Tracked Express Delivery").charge(5D).minDaysRequired(1).maxDaysRequired(1).build()),
                    cartDataMapper.map(DeliveryOption.builder().method("FREE Delivery").charge(0D).minDaysRequired(5).maxDaysRequired(7).build())
            );
        } else {
            return Arrays.asList(
                    cartDataMapper.map(DeliveryOption.builder().method("FREE Delivery").charge(0D).minDaysRequired(3).maxDaysRequired(5).build()),
                    cartDataMapper.map(DeliveryOption.builder().method("Tracked Express Delivery").charge(3D).minDaysRequired(1).maxDaysRequired(1).build())
            );
        }
    }

}
