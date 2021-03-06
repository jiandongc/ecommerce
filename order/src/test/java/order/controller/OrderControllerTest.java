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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;

public class OrderControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8082/orders/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldCreateOrder() {
        // Given
        final String payload = "{\n" +
                "  \"customerId\": \"123e4567-e89b-42d3-a456-556642440000\",\n" +
                "  \"email\": \"abc@db.com\",\n" +
                "  \"items\": 20.2,\n" +
                "  \"postage\": 3,\n" +
                "  \"discount\": -2,\n" +
                "  \"totalBeforeVat\": 21.2,\n" +
                "  \"itemsVat\": 0.3,\n" +
                "  \"postageVat\": 0.2,\n" +
                "  \"discountVat\": -0.1,\n" +
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
                "      \"vatRate\": 20,\n" +
                "      \"vat\": 0.7,\n" +
                "      \"sale\": 3.5,\n" +
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
                "      \"vatRate\": 20,\n" +
                "      \"vat\": 0.73,\n" +
                "      \"sale\": 3.67,\n" +
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
        assertThat(orderOptional.get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")));
        assertThat(orderOptional.get().getEmail(), is("abc@db.com"));
        assertThat(orderOptional.get().getDeliveryMethod(), is("Standard Delivery"));
        assertThat(orderOptional.get().getOrderItems().size(), is(2));
        assertThat(orderOptional.get().getOrderItems().get(0).getVatRate(), is(20));
        assertThat(orderOptional.get().getOrderItems().get(1).getVatRate(), is(20));
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
                "   \"discount\":0,\n" +
                "   \"totalBeforeVat\":5.83,\n" +
                "   \"itemsVat\":0.67,\n" +
                "   \"postageVat\":0.5,\n" +
                "   \"discountVat\":0,\n" +
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
                "         \"vatRate\": 20,\n" +
                "         \"vat\": 0.7,\n" +
                "         \"sale\": 3.5,\n" +
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
        assertThat(orderOptional.get().getCustomerUid(), is(nullValue()));
        assertThat(orderOptional.get().getEmail(), is("abc@gmail.com"));
        assertThat(orderOptional.get().getDeliveryMethod(), is("Standard Delivery"));
        assertThat(orderOptional.get().getOrderItems().size(), is(1));
        assertThat(orderOptional.get().getOrderItems().get(0).getVatRate(), is(20));
        assertThat(orderOptional.get().getOrderAddresses().size(), is(2));
        assertThat(orderOptional.get().getOrderStatuses().size(), is(1));
        assertThat(orderOptional.get().getOrderStatuses().get(0).getStatus(), is("CREATED"));
        assertThat(orderOptional.get().getCurrentStatus(), is("CREATED"));
    }

    @Test
    public void shouldAddOrderStatus() {
        // Given
        Order order = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
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
        Order order = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(BigDecimal.valueOf(0.2)).sale(BigDecimal.valueOf(0.8)).vatRate(20).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);
        final HttpEntity<?> httpEntity = new HttpEntity<Long>(null, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + orderNumber, GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody(), containsString("\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""));
        assertThat(response.getBody(), containsString("\"eta\""));
        assertThat(response.getBody(), containsString("\"currentStatus\":\"CREATED\""));
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
        Order orderOne = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderOne.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderOne.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberOne = orderService.createOrder(orderOne);
        orderService.addOrderStatus(orderNumberOne, OrderStatus.builder().status("Delivered").build());

        Order orderTwo = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderTwo.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderTwo.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberTwo = orderService.createOrder(orderTwo);

        Order orderThree = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")).minDaysRequired(1).maxDaysRequired(3)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        orderThree.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vat(ONE).sale(ONE).vatRate(20).build());
        orderThree.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumberThree = orderService.createOrder(orderThree);
        orderService.addOrderStatus(orderNumberThree, OrderStatus.builder().status("PAYMENT SUCCEEDED").build());

        final HttpEntity<?> httpEntity = new HttpEntity<Long>(null, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "?customerId=123e4567-e89b-42d3-a456-556642440000", GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(3));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberOne), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberTwo), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), orderNumberThree), is(1));

        // When
        ResponseEntity<String> responseTwo = rest.exchange(BASE_URL + "?customerId=123e4567-e89b-42d3-a456-556642440000&status=open", GET, httpEntity, String.class);

        // Then
        assertThat(responseTwo.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(2));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), orderNumberOne), is(0));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), orderNumberTwo), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseTwo.getBody(), orderNumberThree), is(1));

        // When
        ResponseEntity<String> responseThree = rest.exchange(BASE_URL + "?customerId=123e4567-e89b-42d3-a456-556642440000&status=completed", GET, httpEntity, String.class);

        // Then
        assertThat(responseTwo.getStatusCode(), is(OK));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), "\"customerId\":\"123e4567-e89b-42d3-a456-556642440000\""), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), orderNumberOne), is(1));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), orderNumberTwo), is(0));
        assertThat(StringUtils.countOccurrencesOf(responseThree.getBody(), orderNumberThree), is(0));
    }

    @Test
    public void shouldAddCustomerInfo(){
        // Given
        Order order = Order.builder().customerUid(null).email("joe@gmail.com")
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);

        Order order2 = Order.builder().customerUid(null).email("joe@gmail.com")
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order2.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order2.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber2 = orderService.createOrder(order2);

        Order order3 = Order.builder().customerUid(null).email("mary@gmail.com")
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order3.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order3.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber3 = orderService.createOrder(order3);

        UUID existingCustomerUid = UUID.randomUUID();
        Order order4 = Order.builder().customerUid(existingCustomerUid).email("joe@gmail.com")
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order4.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order4.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber4 = orderService.createOrder(order4);

        // Given
        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-42d3-a456-556642440000\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";

        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/customer", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        assertThat(orderOptional.get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")));
        Optional<Order> orderOptional2 = orderService.findByOrderNumber(orderNumber2);
        assertThat(orderOptional2.get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-42d3-a456-556642440000")));
        Optional<Order> orderOptional3 = orderService.findByOrderNumber(orderNumber3);
        assertThat(orderOptional3.get().getCustomerUid(), is(nullValue()));
        Optional<Order> orderOptional4 = orderService.findByOrderNumber(orderNumber4);
        assertThat(orderOptional4.get().getCustomerUid(), is(existingCustomerUid));
    }

    @Test
    public void shouldRejectRequestToUpdateCustomerInfoWithGuestToken(){
        // Given - guest token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        Order order = Order.builder().customerUid(null)
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        orderService.createOrder(order);

        // Given
        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-42d3-a456-556642440000\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";
        final HttpEntity<String> httpEntity = new HttpEntity<>(customerData, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/customer", POST, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(FORBIDDEN));
    }

    @Test
    public void shouldNotUpdateCustomerInfoIfAlreadySet(){
        UUID existingCustomerUid = UUID.randomUUID();
        Order order = Order.builder().customerUid(existingCustomerUid).email("joe@gmail.com")
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);

        // Given
        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-42d3-a456-556642440000\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";

        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);

        // When
        ResponseEntity<String> response = rest.exchange(BASE_URL + "/customer", POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        Optional<Order> orderOptional = orderService.findByOrderNumber(orderNumber);
        assertThat(orderOptional.get().getCustomerUid(), is(existingCustomerUid));
    }

}