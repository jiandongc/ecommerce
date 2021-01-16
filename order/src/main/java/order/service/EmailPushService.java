package order.service;

import email.data.GoogleReviewRequestData;
import email.data.OrderConfirmationData;
import email.data.OrderShippedData;
import email.service.EmailService;
import order.domain.Order;
import order.mapper.GoogleReviewRequestDataMapper;
import order.mapper.OrderConfirmationDataMapper;
import order.mapper.OrderShippedDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailPushService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderConfirmationDataMapper orderConfirmationDataMapper;

    @Autowired
    private OrderShippedDataMapper orderShippedDataMapper;

    @Autowired
    private GoogleReviewRequestDataMapper googleReviewRequestDataMapper;

    public void push(String orderNumber, String type) {
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        if (orderOptional.isPresent() && type.equalsIgnoreCase("order-confirmation")) {
            Order order = orderOptional.get();
            OrderConfirmationData orderConfirmationData = orderConfirmationDataMapper.map(order, "", "Noodle Monster", "https://noodle-monster.co.uk/#!/register/", "https://noodle-monster.co.uk/#!/home");
            emailService.sendMessage(orderConfirmationData);
        } else if (orderOptional.isPresent() && type.equalsIgnoreCase("order-shipped")) {
            Order order = orderOptional.get();
            OrderShippedData orderShippedData = orderShippedDataMapper.map(order);
            emailService.sendMessage(orderShippedData);
        } else if (orderOptional.isPresent() && type.equalsIgnoreCase("google-review-request")){
            Order order = orderOptional.get();
            GoogleReviewRequestData googleReviewRequestData = googleReviewRequestDataMapper.map(order);
            emailService.sendMessage(googleReviewRequestData);
        }
    }
}
