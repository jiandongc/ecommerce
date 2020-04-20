package order.controller;

import order.domain.Order;
import order.domain.OrderStatus;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> save(@RequestBody Order order) {
        return new ResponseEntity<>(orderService.createOrder(order), CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{orderNumber}/status", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addOrderStatus(@PathVariable String orderNumber, @RequestBody OrderStatus orderStatus) {
        orderService.addOrderStatus(orderNumber, orderStatus);
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(o -> new ResponseEntity<>(o, CREATED)).orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "{orderNumber}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(o -> new ResponseEntity<>(o, OK)).orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(value = "{orderNumber}/customer", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addCustomerInfo(@PathVariable String orderNumber, @RequestBody String customerId) {
        orderService.addCustomerInfo(orderNumber, Long.valueOf(customerId));
        Optional<Order> order = orderService.findByOrderNumber(orderNumber);
        return order.map(o -> new ResponseEntity<>(o, CREATED)).orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrdersForCustomer(@RequestParam(value = "customerId") Long customerId,
                                            @RequestParam(value = "status", required = false) String status) {
        return orderService.findOrders(customerId, status);
    }
}
