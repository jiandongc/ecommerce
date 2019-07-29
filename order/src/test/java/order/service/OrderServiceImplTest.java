package order.service;

import order.domain.Order;
import order.domain.OrderStatus;
import order.repository.OrderRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Test
    public void shouldReturnOrders() {
        // Given
        Order orderOne = new Order();
        orderOne.addOrderStatus(OrderStatus.builder().status("Placed").build());
        Order orderTwo = new Order();
        orderTwo.addOrderStatus(OrderStatus.builder().status("Processing").build());
        Order orderThree = new Order();
        orderThree.addOrderStatus(OrderStatus.builder().status("Shipped").build());
        Order orderFour = new Order();
        orderFour.addOrderStatus(OrderStatus.builder().status("Delivered").build());
        Order orderFive = new Order();
        orderFive.addOrderStatus(OrderStatus.builder().status("Returned").build());
        Mockito.when(orderRepository.findByCustomerIdOrderByOrderDateDesc(1L)).thenReturn(Arrays.asList(orderOne, orderTwo, orderThree, orderFour, orderFive));

        // When
        List<Order> openOrders = orderServiceImpl.findOrders(1L, "open");

        // Then
        assertThat(openOrders.size(), CoreMatchers.is(3));
        assertThat(openOrders.get(0).getCurrentStatus(), CoreMatchers.is("Placed"));
        assertThat(openOrders.get(1).getCurrentStatus(), CoreMatchers.is("Processing"));
        assertThat(openOrders.get(2).getCurrentStatus(), CoreMatchers.is("Shipped"));

        // When
        List<Order> completedOrders = orderServiceImpl.findOrders(1L, "completed");

        // Then
        assertThat(completedOrders.size(), CoreMatchers.is(2));
        assertThat(completedOrders.get(0).getCurrentStatus(), CoreMatchers.is("Delivered"));
        assertThat(completedOrders.get(1).getCurrentStatus(), CoreMatchers.is("Returned"));

        // When
        List<Order> allOrders = orderServiceImpl.findOrders(1L, null);

        // Then
        assertThat(allOrders.size(), CoreMatchers.is(5));
    }

}