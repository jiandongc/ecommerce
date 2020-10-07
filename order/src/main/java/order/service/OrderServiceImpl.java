package order.service;

import order.domain.Order;
import order.domain.OrderStatus;
import order.repository.OrderRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public String createOrder(Order order) {
        validateOrder(order);
        order.addOrderStatus(OrderStatus.builder().status("CREATED").description("Order created").creationTime(LocalDateTime.now()).build());
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
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);

        orderOptional.ifPresent(order -> {
            Hibernate.initialize(order.getOrderItems());
            Hibernate.initialize(order.getOrderAddresses());
            Hibernate.initialize(order.getOrderStatuses());
        });

        return orderOptional;
    }

    @Override
    @Transactional
    public void addCustomerInfo(String email, UUID customerUid) {
        List<Order> orders = orderRepository.findByEmail(email);
        orders.stream().filter(order -> order.getCustomerUid() == null).forEach(order -> order.setCustomerUid(customerUid));
    }

    @Override
    @Transactional
    public void addOrderStatus(String orderNumber, OrderStatus orderStatus) {
        Optional<Order> orderOptional = orderRepository.findByOrderNumber(orderNumber);
        orderStatus.setCreationTime(LocalDateTime.now());
        orderOptional.ifPresent(order -> order.addOrderStatus(orderStatus));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findOrders(UUID customerUid, String status) {
        final List<Order> orders = orderRepository.findByCustomerUidOrderByOrderDateDesc(customerUid);
        if ("open".equalsIgnoreCase(status)) {
            return orders.stream().filter(order -> order.getCurrentStatus().toLowerCase().matches("created|processing|shipped|payment succeeded")).collect(Collectors.toList());
        } else if ("completed".equalsIgnoreCase(status)) {
            return orders.stream().filter(order -> order.getCurrentStatus().toLowerCase().matches("delivered|returned|cancelled|failed")).collect(Collectors.toList());
        }

        return orders;
    }

    private void validateOrder(Order order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("OrderItems is empty for customer: " + order.getCustomerUid());
        }

        if (order.getOrderAddresses() == null || order.getOrderAddresses().isEmpty()) {
            throw new RuntimeException("OrderAddress is empty for customer: " + order.getCustomerUid());
        }
    }
}
