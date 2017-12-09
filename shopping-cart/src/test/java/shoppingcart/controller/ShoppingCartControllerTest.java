package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import shoppingcart.data.CartSummary;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ShoppingCartControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private ShoppingCartRepository repository;

    @Test
    public void shouldCreateShoppingCartForUser(){
        // Given & When
        final HttpEntity<Long> payload = new HttpEntity<Long>(123L, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final UUID uuid = UUID.fromString(response.getBody());
        final ShoppingCart shoppingCart = repository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerId(), is(123L));
    }

    @Test
    public void shouldCreateShoppingCartForGuest(){
        // Given & When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final UUID uuid = UUID.fromString(response.getBody());
        final ShoppingCart shoppingCart = repository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));;
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerId(), is(nullValue()));
    }

    @Test
    public void shouldRejectCreateShoppingCartRequestIfTokenIsExpired(){
        // Given
        headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        // user token which has been expired
        headers.add("Cookie", "access_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjoxNTEyNjg4NjQxfQ.ERFgX9cq2XmFAsdLiTl-LsapwNlBsoxmGD2nkY5Y66Izhpd8gasVd-Cjml9EQIj4KHMgpYbDl9CMgSo28LogHA");
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);

        // When
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(INTERNAL_SERVER_ERROR));
    }

    @Test
    public void shouldRetrieveShoppingCartByUid(){
        // Given
        final UUID cartUid = repository.create();
        final String itemOne = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\"\n" +
                "}";
        final HttpEntity<String> itemOnePayload = new HttpEntity<String>(itemOne, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartSummary.class);
        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\"\n" +
                "}";
        final HttpEntity<String> itemTwoPayload = new HttpEntity<String>(itemTwo, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartSummary.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartSummary.class);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartSummary> response = rest.exchange(BASE_URL + cartUid.toString(), GET, payload, CartSummary.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getTotalQuantity(), is(3));
        assertThat(response.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(21)));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().size(), is(2));
    }

    @Test
    public void shouldReturn404IfCartUidIsValid(){
        // Given
        final UUID cartUid = UUID.randomUUID();

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartSummary> response = rest.exchange(BASE_URL + cartUid.toString(), GET, payload, CartSummary.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }



}