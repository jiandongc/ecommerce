package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shoppingcart.data.OrderData;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.mapper.OrderDataMapper;
import shoppingcart.service.ShoppingCartService;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/carts/order-data")
public class OrderDataController {

    private final ShoppingCartService shoppingCartService;
    private final OrderDataMapper orderDataMapper;

    @Autowired
    public OrderDataController(ShoppingCartService shoppingCartService, OrderDataMapper orderDataMapper) {
        this.shoppingCartService = shoppingCartService;
        this.orderDataMapper = orderDataMapper;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{cartUid}", method = GET)
    public ResponseEntity<OrderData> getOrderDataByUid(@PathVariable UUID cartUid) {
        final Optional<ShoppingCart> cartOptional = shoppingCartService.getShoppingCartByUid(cartUid);
        return cartOptional.map(cart -> new ResponseEntity<>(orderDataMapper.map(cart), OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
