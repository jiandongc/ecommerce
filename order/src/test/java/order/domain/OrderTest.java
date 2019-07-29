package order.domain;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;

public class OrderTest {

    @Test
    public void shouldReturnCurrentStatus() throws Exception {
        Order order = new Order();
        assertThat(order.getCurrentStatus(), CoreMatchers.is(""));
        Thread.sleep(100);

        order.addOrderStatus(OrderStatus.builder().creationTime(LocalDateTime.now()).status("Placed").build());
        assertThat(order.getCurrentStatus(), CoreMatchers.is("Placed"));
        Thread.sleep(100);

        order.addOrderStatus(OrderStatus.builder().creationTime(LocalDateTime.now()).status("Processing").build());
        assertThat(order.getCurrentStatus(), CoreMatchers.is("Processing"));
        Thread.sleep(100);

        order.addOrderStatus(OrderStatus.builder().creationTime(LocalDateTime.now()).status("Delivered").build());
        assertThat(order.getCurrentStatus(), CoreMatchers.is("Delivered"));
    }

}