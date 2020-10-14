package order.controller;

import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderItem;
import order.domain.OrderStatus;
import order.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;

public class AdminControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldFindOrdersByStatus() {
        // Given
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        Order orderOne = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderOne.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderOne.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberOne = orderService.createOrder(orderOne);
        orderService.addOrderStatus(orderNumberOne, OrderStatus.builder().status("created").build());

        Order orderTwo = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderTwo.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderTwo.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberTwo = orderService.createOrder(orderTwo);
        orderService.addOrderStatus(orderNumberTwo, OrderStatus.builder().status("payment succeeded").build());

        Order orderThree = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderThree.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderThree.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberThree = orderService.createOrder(orderThree);
        orderService.addOrderStatus(orderNumberThree, OrderStatus.builder().status("delivered").build());

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/admin/orders?status=created&sort=date.asc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));

        // When & Then
        response = rest.exchange("/admin/orders?status=payment succeeded&sort=date.asc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberTwo), is(1));

        // When & Then
        response = rest.exchange("/admin/orders?status=delivered&sort=date.asc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberThree), is(1));

        // When & Then
        response = rest.exchange("/admin/orders?status=open&sort=date.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(2));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberTwo), is(1));

        // When & Then
        response = rest.exchange("/admin/orders?status=completed&sort=date.desc", GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberThree), is(1));
    }

    @Test
    public void shouldFindOrdersByDate() {
        // Given
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        Order orderOne = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderOne.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderOne.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberOne = orderService.createOrder(orderOne);
        orderService.addOrderStatus(orderNumberOne, OrderStatus.builder().status("created").build());

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final LocalDate today = LocalDate.now();
        final LocalDate yesterday = today.minusDays(1);
        final LocalDate tomorrow = today.plusDays(1);
        ResponseEntity<String> response = rest.exchange("/admin/orders?date=" + today.toString(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));

        // When & Then
        response = rest.exchange("/admin/orders?date=" + yesterday.toString(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(0));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(0));

        // When & Then
        response = rest.exchange("/admin/orders?date=" + tomorrow.toString(), GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(0));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(0));
    }

    @Test
    public void shouldFindOrdersByOrderNumber() {
        // Given
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        Order orderOne = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderOne.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderOne.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberOne = orderService.createOrder(orderOne);
        orderService.addOrderStatus(orderNumberOne, OrderStatus.builder().status("created").build());

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rest.exchange("/admin/orders?orderNumber=" + orderNumberOne, GET, httpEntity, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));
    }

}