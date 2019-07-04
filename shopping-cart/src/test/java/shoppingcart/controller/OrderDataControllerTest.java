package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import shoppingcart.data.CartData;
import shoppingcart.data.OrderData;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;


public class OrderDataControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/orders/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldGetCartInOrderFormat(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(1.3d)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);
        shoppingCartItemRepository.updateQuantity(cart.getId(), "109283", 9);
        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(7.3d)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);
        shoppingCartItemRepository.updateQuantity(cart.getId(), "219283", 6);
        final DeliveryOption deliveryOption = DeliveryOption.builder()
                .method("Express Delivery")
                .charge(1.1d)
                .minDaysRequired(3)
                .maxDaysRequired(5)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addDeliveryOption(cart.getId(), deliveryOption);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<OrderData> response = rest.exchange(BASE_URL + uuid.toString(), GET, payload, OrderData.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getItems(), is(BigDecimal.valueOf(46.25)));
        assertThat(response.getBody().getPostage(), is(BigDecimal.valueOf(0.92)));
        assertThat(response.getBody().getTotalBeforeVat(), is(BigDecimal.valueOf(47.17)));
        assertThat(response.getBody().getItemsVat(), is(BigDecimal.valueOf(9.25)));
        assertThat(response.getBody().getPostageVat(), is(BigDecimal.valueOf(0.18)));
        assertThat(response.getBody().getTotalVat(), is(BigDecimal.valueOf(9.43)));
        assertThat(response.getBody().getOrderTotal(), is(BigDecimal.valueOf(56.6)));
        assertThat(response.getBody().getDeliveryMethod(), is("Express Delivery"));
        assertThat(response.getBody().getMinDaysRequired(), is(3));
        assertThat(response.getBody().getMaxDaysRequired(), is(5));
        assertThat(response.getBody().getOrderItems().size(), is(2));
    }

}