package order.service;

import order.domain.Order;
import order.repository.OrderRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public String createOrder(Order order) {
        order.setOrderNumber(OrderUtils.generateOrderNumber());
        order.setOrderDate(LocalDate.now());
        order.setCreationTime(LocalDateTime.now());
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));
        order.getOrderAddresses().forEach(orderAddress -> orderAddress.setOrder(order));
        Order saved = orderRepository.save(order);
        return saved.getOrderNumber();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order != null) {
            Hibernate.initialize(order.getOrderItems());
            Hibernate.initialize(order.getOrderAddresses());
            Hibernate.initialize(order.getOrderStatuses());
            return Optional.of(order);
        } else {
            return Optional.empty();
        }

    }
}
