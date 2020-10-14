package order.service;

import order.domain.Order;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

public class OrderPredicate {

    public static Predicate<Order> statusFilter(String status) {
        return order -> (status != null && status.equals(order.getCurrentStatus().toLowerCase()))
                || ("open".equalsIgnoreCase(status) && order.getCurrentStatus().toLowerCase().matches("created|processing|shipped|payment succeeded|payment failed"))
                || ("completed".equalsIgnoreCase(status) && order.getCurrentStatus().toLowerCase().matches("delivered|returned|cancelled|failed"));
    }

    public static Predicate<Order> orderDateFilter(LocalDate orderDate) {
        return order -> order.getOrderDate().equals(orderDate);
    }

    public static Comparator<Order> orderDateAscComparator(){
        return Comparator.comparing(Order::getOrderDate);
    }

    public static Comparator<Order> orderDateDescComparator(){
        return Comparator.comparing(Order::getOrderDate).reversed();
    }
}
