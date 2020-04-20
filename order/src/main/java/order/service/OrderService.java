package order.service;

import order.domain.Order;
import order.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    String createOrder(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
    void addCustomerInfo(String orderNumber, long customerId);
    void addOrderStatus(String orderNumber, OrderStatus orderStatus);
    List<Order> findOrders(Long customerId, String status);
}
