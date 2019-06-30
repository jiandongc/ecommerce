package order.controller;

import order.domain.Order;
import order.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;

public class OrderControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8082/orders/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private OrderService orderService;

    @Test
    public void shouldCreateOrder(){
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
        assertThat(orderOptional.get().getOrderItems().get(0).getSku(), is("sku"));
        assertThat(orderOptional.get().getOrderItems().get(1).getSku(), is("sku1"));
    }

}