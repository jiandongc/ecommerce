package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.data.SimpleCartData;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.SimpleCartDataMapper;
import shoppingcart.service.ShoppingCartEmailService;
import shoppingcart.service.ShoppingCartService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/admin/carts")
public class AdminController {

    private final ShoppingCartService shoppingCartService;
    private final SimpleCartDataMapper simpleCartDataMapper;
    private final ShoppingCartEmailService shoppingCartEmailService;

    @Autowired
    public AdminController(ShoppingCartService shoppingCartService,
                           SimpleCartDataMapper simpleCartDataMapper,
                           ShoppingCartEmailService shoppingCartEmailService) {
        this.shoppingCartService = shoppingCartService;
        this.simpleCartDataMapper = simpleCartDataMapper;
        this.shoppingCartEmailService = shoppingCartEmailService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimpleCartData> getAllShoppingCarts(@RequestParam(value = "date", required = false) String date) throws Exception {
        List<ShoppingCart> shoppingCarts = shoppingCartService.findShoppingCarts(date != null ? new SimpleDateFormat("yyyy-MM-dd").parse(date) : null);
        return shoppingCarts.stream().map(simpleCartDataMapper::map).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "{cartUid}", method = DELETE)
    public void deleteShoppingCart(@PathVariable UUID cartUid) {
        shoppingCartService.deleteShoppingCart(cartUid);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "email", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity pushEmails(@RequestParam(value = "type") String type,
                                     @RequestParam(value = "cartUid") String cartUid) {

        if (type.equalsIgnoreCase("abandoned-cart-notification")) {
            Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.getShoppingCartByUid(UUID.fromString(cartUid));
            shoppingCartOptional.ifPresent(cart -> shoppingCartEmailService.sendAbandonedCartNotification(cart.getEmail(), cart.getCartUid().toString(), cart.getShoppingCartItems()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
