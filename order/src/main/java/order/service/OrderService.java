package order.service;

import order.domain.Order;

import java.util.Optional;

public interface OrderService {
    String createOrder(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
}
