package order.controller;

import order.domain.Order;
import order.service.EmailPushService;
import order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/admin/orders")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailPushService emailPushService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrders(@RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "date", required = false) String date,
                                 @RequestParam(value = "orderNumber", required = false) String orderNumber,
                                 @RequestParam(value = "sort", required = false) String sort){
        return orderService.findOrders(status, date, orderNumber, sort);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "email", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity pushEmails(@RequestParam(value = "type") String type,
                                     @RequestParam(value = "orderNumber") String orderNumber,
                                     @RequestParam(value = "voucherCode", required = false) String voucherCode){

        if (type.equalsIgnoreCase("google-review-request") && voucherCode != null) {
            emailPushService.pushGoogleReviewRequestMailWithVoucherCode(orderNumber, voucherCode);
        } else {
            emailPushService.push(orderNumber, type);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
