package order.service;

import email.data.OrderConfirmationData;
import email.service.EmailService;
import order.domain.Order;
import order.mapper.OrderConfirmationDataMapper;
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

    public void push(String orderNumber, String type) {
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        if (orderOptional.isPresent() && type.equalsIgnoreCase("order-confirmation")) {
            Order order = orderOptional.get();
            OrderConfirmationData orderConfirmationData = orderConfirmationDataMapper.map(order, "", "Noodle Monster", "https://noodle-monster.co.uk/#!/register/", "https://noodle-monster.co.uk/#!/home");
            emailService.sendMessage(orderConfirmationData);
        }
    }
}
