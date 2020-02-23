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
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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
                "  \"email\": \"abc@db.com\",\n" +
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
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL, POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        Optional<Order> orderOptional = orderService.findByOrderNumber(response.getBody());
        assertThat(orderOptional.isPresent(), is(true));
        assertThat(orderOptional.get().getCustomerId(), is(1L));
        assertThat(orderOptional.get().getEmail(), is("abc@db.com"));
        assertThat(orderOptional.get().getDeliveryMethod(), is("Standard Delivery"));
        assertThat(orderOptional.get().getOrderItems().size(), is(2));
        assertThat(orderOptional.get().getOrderAddresses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().size(), is(1));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("CREATED"));
        assertThat(orderOptional.get().getCurrentStatus(), is("CREATED"));
    }

    @Test
    public void shouldCreateGuestOrder() {
        // Given
        final String payload = "{  \n" +
                "   \"customerId\":null,\n" +
                "   \"email\":\"abc@gmail.com\",\n" +
                "   \"items\":3.33,\n" +
                "   \"postage\":2.5,\n" +
                "   \"promotion\":0,\n" +
                "   \"totalBeforeVat\":5.83,\n" +
                "   \"itemsVat\":0.67,\n" +
                "   \"postageVat\":0.5,\n" +
                "   \"promotionVat\":0,\n" +
                "   \"totalVat\":1.17,\n" +
                "   \"orderTotal\":7,\n" +
                "   \"deliveryMethod\":\"Standard Delivery\",\n" +
                "   \"minDaysRequired\":3,\n" +
                "   \"maxDaysRequired\":5,\n" +
                "   \"orderItems\":[  \n" +
                "      {  \n" +
                "         \"sku\":\"sku00013\",\n" +
                "         \"name\":\"Personalised Angel Wings Sleepsuit - Pink\",\n" +
                "         \"price\":4,\n" +
                "         \"quantity\":1,\n" +
                "         \"thumbnail\":null,\n" +
                "         \"description\":\"\",\n" +
                "         \"code\":\"mzg0001695\",\n" +
                "         \"imageUrl\":\"/images/ps0013.jpg\",\n" +
                "         \"subTotal\":4\n" +
                "      }\n" +
                "   ],\n" +
                "   \"orderAddresses\":[  \n" +
                "      {  \n" +
                "         \"addressType\":\"Shipping\",\n" +
                "         \"name\":\"Jiandong Chen\",\n" +
                "         \"title\":\"Mr\",\n" +
                "         \"mobile\":\"07745324432\",\n" +
                "         \"addressLine1\":\"5 NEW FARM AVENUE\",\n" +
                "         \"addressLine2\":null,\n" +
                "         \"addressLine3\":null,\n" +
                "         \"city\":\"BROMLEY\",\n" +
                "         \"country\":\"United Kingdom\",\n" +
                "         \"postcode\":\"BR20TX\"\n" +
                "      },\n" +
                "      {  \n" +
                "         \"addressType\":\"Billing\",\n" +
                "         \"name\":\"Jiandong Chen\",\n" +
                "         \"title\":\"Mr\",\n" +
                "         \"mobile\":\"07745324432\",\n" +
                "         \"addressLine1\":\"5 NEW FARM AVENUE\",\n" +
                "         \"addressLine2\":null,\n" +
                "         \"addressLine3\":null,\n" +
                "         \"city\":\"BROMLEY\",\n" +
                "         \"country\":\"United Kingdom\",\n" +
                "         \"postcode\":\"BR20TX\"\n" +
                "      }\n" +
                "   ]\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL, POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        Optional<Order> orderOptional = orderService.findByOrderNumber(response.getBody());
        assertThat(orderOptional.isPresent(), is(true));
        assertThat(orderOptional.get().getCustomerId(), is(nullValue()));
        assertThat(orderOptional.get().getEmail(), is("abc@gmail.com"));
        assertThat(orderOptional.get().getDeliveryMethod(), is("Standard Delivery"));
        assertThat(orderOptional.get().getOrderItems().size(), is(1));
        assertThat(orderOptional.get().getOrderAddresses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().size(), is(1));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("CREATED"));
        assertThat(orderOptional.get().getCurrentStatus(), is("CREATED"));
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
                "  \"status\": \"Processing\",\n" +
                "  \"description\": \"paid successfully\"\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + orderNumber + "/status", POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        assertThat(orderOptional.get().getOrderStatuses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("CREATED"));
        assertThat(orderOptional.get().getOrderStatuses().get(1).getStatus(), is("Processing"));
        assertThat(orderOptional.get().getOrderStatuses().get(1).getDescription(), is("paid successfully"));
        assertThat(orderOptional.get().getCurrentStatus(), is("Processing"));
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

    @Test
    public void shouldReturnOrderByOrderNumber() {
        // Given
        Order order = Order.builder().customerId(123L).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).promotion(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).promotionVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);
        final HttpEntity<?> httpEntity = new HttpEntity<Long>(null, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + orderNumber, GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), containsString("\"customerId\":123"));
        assertThat(response.getBody(), containsString("\"eta\""));
    }

    @Test
    public void shouldReturn404IfOrderNumberDoesNotExist() {
        // Given
        final HttpEntity<?> httpEntity = new HttpEntity<Long>(null, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "12345", GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldReturnOrdersByCustomerId() {
        // Given
        Order orderOne = Order.builder().customerId(123L).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).promotion(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).promotionVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderOne.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).build());
        orderOne.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberOne = orderService.createOrder(orderOne);
        orderService.addOrderStatus(orderNumberOne, OrderStatus.builder().status("Delivered").build());

        Order orderTwo = Order.builder().customerId(123L).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).promotion(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).promotionVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderTwo.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).build());
        orderTwo.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberTwo = orderService.createOrder(orderTwo);

        final HttpEntity<?> httpEntity = new HttpEntity<Long>(null, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?customerId=123", GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":123"), is(2));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberTwo), is(1));

        // When
        ResponseEntity<String> responseTwo = rest.exchange(BASE_URL + "?customerId=123&status=open", GET, httpEntity, String.class);

        // Then
        assertThat(responseTwo.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), "\"customerId\":123"), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), orderNumberOne), is(0));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), orderNumberTwo), is(1));

        // When
        ResponseEntity<String> responseThree = rest.exchange(BASE_URL + "?customerId=123&status=completed", GET, httpEntity, String.class);

        // Then
        assertThat(responseTwo.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), "\"customerId\":123"), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), orderNumberOne), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), orderNumberTwo), is(0));
    }

}