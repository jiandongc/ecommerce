package order.service;

import order.domain.Order;
import order.domain.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    String createOrder(Order order);
    Optional<Order> findByOrderNumber(String orderNumber);
    void addCustomerInfo(String orderNumber, UUID customerUid);
    void addOrderStatus(String orderNumber, OrderStatus orderStatus);
    List<Order> findOrders(UUID customerUid, String status);
    List<Order> findOrders(String status, String date, String orderNumber, String sort);
}
