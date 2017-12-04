package shoppingcart.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ShoppingCartItemControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/";
    private final TestRestTemplate rest = new TestRestTemplate();
    private HttpHeaders headers;

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ShoppingCartItemRepository itemRepository;

    @Before
    public void before() {
        this.reset();
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(APPLICATION_JSON);
        }
    }

    @Test
    public void shouldAddItemToCart(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String json = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"10.99\",\n" +
                "\"imageUrl\": \"/cloth.jpeg\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        ResponseEntity<ShoppingCart> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, ShoppingCart.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final List<ShoppingCartItem> cartItems = itemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getSku(), is("123456"));
        assertThat(cartItems.get(0).getName(), is("kid's cloth"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(10.99)));
        assertThat(cartItems.get(0).getImageUrl(), is("/cloth.jpeg"));
        assertThat(cartItems.get(0).getQuantity(), is(1));
    }

    @Test
    public void shouldReturn409IfCartUidIsNotFound(){
        // Given
        final String json = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"10.99\",\n" +
                "\"imageUrl\": \"/cloth.jpeg\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        ResponseEntity<ShoppingCart> response = rest.exchange(BASE_URL + randomUUID().toString() + "/items", POST, payload, ShoppingCart.class);

        // Then
        assertThat(response.getStatusCode(), is(CONFLICT));
        assertThat(response.getBody(), is(nullValue()));
    }

    @Test
    public void shouldBeAbleToAddTheSameItemToCartMultipleTimes(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String json = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"10.99\",\n" +
                "\"imageUrl\": \"/cloth.jpeg\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, ShoppingCart.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, ShoppingCart.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, ShoppingCart.class);
        ResponseEntity<ShoppingCart> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, ShoppingCart.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final List<ShoppingCartItem> cartItems = itemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getSku(), is("123456"));
        assertThat(cartItems.get(0).getName(), is("kid's cloth"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(10.99)));
        assertThat(cartItems.get(0).getImageUrl(), is("/cloth.jpeg"));
        assertThat(cartItems.get(0).getQuantity(), is(4));
    }



}