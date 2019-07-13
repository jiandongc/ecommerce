package order.service;

import order.domain.Order;
import order.domain.OrderStatus;

import java.util.Optional;

public interface OrderService {
    String createOrder(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
    void addOrderStatus(String orderNumber, OrderStatus orderStatus);
}
