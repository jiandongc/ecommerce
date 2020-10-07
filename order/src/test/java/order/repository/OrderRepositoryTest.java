package order.repository;

import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderItem;
import order.domain.OrderStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void shouldSaveOrder(){
        // Given
        final Order order = Order.builder()
                .orderNumber("1111-2222-3333")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 30))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();

        order.addOrderItem(OrderItem.builder().code("code").price(new BigDecimal("1.0")).quantity(2).sku("sku").name("name").subTotal(new BigDecimal("2.0")).vatRate(20).vat(new BigDecimal("0.4")).sale(new BigDecimal("1.6")).build());
        order.addOrderStatus(OrderStatus.builder().status("PAID").creationTime(LocalDateTime.now()).description("description").build());
        order.addOrderAddress(OrderAddress.builder().addressType("Billing").title("Mr.").name("John").mobile("04838473883").addressLine1("line 1").addressLine2("line 2").addressLine3("line 3").city("Manchester").country("United Kingdom").postcode("M2 1DD").build());

        // When
        orderRepository.save(order);

        // Then
        Order actual = orderRepository.getOne(order.getId());
        assertThat(actual.getOrderNumber(), is("1111-2222-3333"));
        assertThat(actual.getCustomerUid(), is(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")));
        assertThat(actual.getOrderStatuses().size(), is(1));
        assertThat(actual.getOrderStatuses().get(0).getStatus(), is("PAID"));
        assertThat(actual.getOrderItems().size(), is(1));
        assertThat(actual.getOrderItems().get(0).getPrice(), is(new BigDecimal("1.0")));
        assertThat(actual.getOrderItems().get(0).getQuantity(), is(2));
        assertThat(actual.getOrderItems().get(0).getVatRate(), is(20));
        assertThat(actual.getOrderItems().get(0).getVat(), is(new BigDecimal("0.4")));
        assertThat(actual.getOrderItems().get(0).getSale(), is(new BigDecimal("1.6")));
        assertThat(actual.getOrderAddresses().size(), is(1));
        assertThat(actual.getOrderAddresses().get(0).getAddressType(), is("Billing"));
        assertThat(actual.getOrderAddresses().get(0).getName(), is("John"));
        assertThat(actual.getOrderAddresses().get(0).getAddressLine1(), is("line 1"));
        assertThat(actual.getOrderAddresses().get(0).getPostcode(), is("M2 1DD"));
    }

    @Test
    public void shouldFindOrderByCustomerUid(){
        final Order orderOne = Order.builder()
                .orderNumber("20191230")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 30))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderOne);

        final Order orderTwo = Order.builder()
                .orderNumber("20191229")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 29))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderTwo);

        final Order orderThree = Order.builder()
                .orderNumber("20191231")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 31))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderThree);

        // When & Then
        List<Order> orders = orderRepository.findByCustomerUidOrderByOrderDateDesc(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"));
        assertThat(orders.size(), is(3));
        assertThat(orders.get(0).getOrderNumber(), is("20191231"));
        assertThat(orders.get(1).getOrderNumber(), is("20191230"));
        assertThat(orders.get(2).getOrderNumber(), is("20191229"));

        orders = orderRepository.findByCustomerUidOrderByOrderDateDesc(UUID.fromString("123e4567-e89b-42d3-a456-556642441111"));
        assertThat(orders.size(), is(0));

    }


    @Test
    public void shouldFindOrderByEmail(){
        final Order orderOne = Order.builder()
                .orderNumber("20191230")
                .email("joe@gmail.com")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 30))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderOne);

        final Order orderTwo = Order.builder()
                .orderNumber("20191229")
                .email("joe@gmail.com")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 29))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderTwo);

        final Order orderThree = Order.builder()
                .orderNumber("20191231")
                .email("mary@gmail.com")
                .customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(new BigDecimal("20.2"))
                .postage(new BigDecimal("3.0"))
                .discount(new BigDecimal("-2.0"))
                .totalBeforeVat(new BigDecimal("21.2"))
                .itemsVat(new BigDecimal("0.3"))
                .postageVat(new BigDecimal("0.2"))
                .discountVat(new BigDecimal("-0.1"))
                .totalVat(new BigDecimal("0.4"))
                .orderTotal(new BigDecimal("21.6"))
                .orderDate(LocalDate.of(2019, 12, 31))
                .deliveryMethod("Standard Delivery")
                .minDaysRequired(1)
                .maxDaysRequired(3)
                .creationTime(LocalDateTime.now())
                .build();
        orderRepository.save(orderThree);

        // When & Then
        List<Order> orders = orderRepository.findByEmail("joe@gmail.com");
        assertThat(orders.size(), is(2));

        orders = orderRepository.findByEmail("mary@gmail.com");
        assertThat(orders.size(), is(1));

        orders = orderRepository.findByEmail("tom@gmail.com");
        assertThat(orders.size(), is(0));

    }

}