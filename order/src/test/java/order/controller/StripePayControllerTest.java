package order.controller;

import order.domain.Order;
import order.domain.OrderAddress;
import order.domain.OrderItem;
import order.service.OrderService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class StripePayControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldDownloadStripeClientSecret() {
        // Given
        Order order = Order.builder().customerUid(UUID.fromString("123e4567-e89b-42d3-a456-556642440000"))
                .items(ONE).postage(ONE).discount(ONE).totalBeforeVat(ONE)
                .itemsVat(ONE).postageVat(ONE).discountVat(ONE).totalVat(ONE).orderTotal(ONE)
                .build();
        order.addOrderItem(OrderItem.builder().sku("sku").code("code").name("name").description("desc").price(ONE).quantity(1).subTotal(ONE).vatRate(20).vat(ONE).sale(ONE).build());
        order.addOrderAddress(OrderAddress.builder().addressType("shipping").name("name").title("Mr.").mobile("000").addressLine1("addressline1").city("city").country("country").postcode("000").build());
        String orderNumber = orderService.createOrder(order);

        String json = "{\n" +
                "  \"shoppingCartId\": \"123-456\",\n" +
                "  \"userName\": \"Joe\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(json, headers);
        final ResponseEntity<String> response = rest.exchange("/orders/stripe/" + orderNumber, HttpMethod.POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), CoreMatchers.is(OK));
        assertThat(response.getBody().startsWith("pi_"), CoreMatchers.is(true));
        assertThat(response.getBody().length() > 10 , CoreMatchers.is(true));
    }

    @Test
    public void shouldProcessPaymentSucceededEvent(){
        String payload = "";

        this.headers =  new HttpHeaders();
        final HttpEntity<String> httpEntity = new HttpEntity<>(payload, headers);

        // When
        ResponseEntity<Void> response = rest.exchange("/orders/stripe/webhook/payment_intents", POST, httpEntity, Void.class);

        // Then
        assertThat(response.getStatusCode(), CoreMatchers.is(BAD_REQUEST));
    }

}