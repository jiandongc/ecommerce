package order.controller;

import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderItem;
import order.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class OrderControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8082/orders/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldCreateOrder() {
        // Given
        final String payload = "{\n" +
                "  \"customerId\": 1,\n" +
                "  \"items\": 20.2,\n" +
                "  \"postage\": 3,\n" +
                "  \"promotion\": -2,\n" +
                "  \"totalBeforeVat\": 21.2,\n" +
                "  \"itemsVat\": 0.3,\n" +
                "  \"postageVat\": 0.2,\n" +
                "  \"promotionVat\": -0.1,\n" +
                "  \"totalVat\": 0.4,\n" +
                "  \"orderTotal\": 21.6,\n" +
                "  \"deliveryMethod\": \"Standard Delivery\",\n" +
                "  \"minDaysRequired\": 1,\n" +
                "  \"maxDaysRequired\": 3,\n" +
                "  \"orderItems\": [\n" +
                "    {\n" +
                "      \"sku\": \"sku\",\n" +
                "      \"code\": \"code\",\n" +
                "      \"name\": \"name\",\n" +
                "      \"description\": \"description\",\n" +
                "      \"price\": 2.1,\n" +
                "      \"quantity\": 2,\n" +
                "      \"subTotal\": 4.2,\n" +
                "      \"imageUrl\": \"http://localhost\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"sku\": \"sku1\",\n" +
                "      \"code\": \"code1\",\n" +
                "      \"name\": \"name1\",\n" +
                "      \"description\": \"description1\",\n" +
                "      \"price\": 2.2,\n" +
                "      \"quantity\": 2,\n" +
                "      \"subTotal\": 4.4,\n" +
                "      \"imageUrl\": \"http://localhost\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"orderAddresses\": [\n" +
                "    {\n" +
                "      \"addressType\": \"Shipping\",\n" +
                "      \"name\": \"John\",\n" +
                "      \"title\": \"Mr.\",\n" +
                "      \"mobile\": \"077777777\",\n" +
                "      \"addressLine1\": \"line 1\",\n" +
                "      \"addressLine2\": \"line 2\",\n" +
                "      \"addressLine3\": \"line 3\",\n" +
                "      \"city\": \"Manchester\",\n" +
                "      \"country\": \"United Kingdom\",\n" +
                "      \"postcode\": \"M2 3DD\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"addressType\": \"Billing\",\n" +
                "      \"name\": \"John\",\n" +
                "      \"title\": \"Mr.\",\n" +
                "      \"mobile\": \"077777777\",\n" +
                "      \"addressLine1\": \"line 1\",\n" +
                "      \"addressLine2\": \"line 2\",\n" +
                "      \"addressLine3\": \"line 3\",\n" +
                "      \"city\": \"Manchester\",\n" +
                "      \"country\": \"United Kingdom\",\n" +
                "      \"postcode\": \"M2 4DD\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<String>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL, POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        Optional<Order> orderOptional = orderService.findByOrderNumber(response.getBody());
        assertThat(orderOptional.isPresent(), is(true));
        assertThat(orderOptional.get().getCustomerId(), is(1L));
        assertThat(orderOptional.get().getDeliveryMethod(), is("Standard Delivery"));
        assertThat(orderOptional.get().getOrderItems().size(), is(2));
        assertThat(orderOptional.get().getOrderAddresses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().size(), is(1));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("NEW"));
    }

    @Test
    public void shouldAddOrderStatus() {
        // Given
        Order order = Order.builder().customerId(123L)
                .items(ONE).postage(ONE).promotion(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).promotionVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);
        String payload = "{\n" +
                "  \"status\": \"PAID\",\n" +
                "  \"description\": \"paid successfully\"\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + orderNumber + "/status", POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        assertThat(orderOptional.get().getOrderStatuses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("NEW"));
        assertThat(orderOptional.get().getOrderStatuses().get(1).getStatus(), is("PAID"));
        assertThat(orderOptional.get().getOrderStatuses().get(1).getDescription(), is("paid successfully"));
    }

    @Test
    public void shouldReturn404IfOrderIsNotFound() {
        // Given
        String payload = "{\n" +
                "  \"status\": \"PAID\",\n" +
                "  \"description\": \"paid successfully\"\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "1234567" + "/status", POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

}