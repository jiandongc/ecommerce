package shoppingcart.controller;

import org.hamcrest.CoreMatchers;
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
        // user token which has been expired
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjoxNTEyNjg4NjQxfQ.ERFgX9cq2XmFAsdLiTl-LsapwNlBsoxmGD2nkY5Y66Izhpd8gasVd-Cjml9EQIj4KHMgpYbDl9CMgSo28LogHA");
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

    @Test
    public void shouldUpdateCustomerUid(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final UUID cartUid = repository.create();

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(12345L, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + cartUid.toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repository.findByUUID(cartUid).get().getCustomerId(), is(12345L));
    }

    @Test
    public void shouldRejectUpdateCustomerUidRequestWithGuestToken(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        final UUID cartUid = repository.create();

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(12345L, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + cartUid.toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
        assertThat(repository.findByUUID(cartUid).get().getCustomerId(), CoreMatchers.nullValue());
    }

    @Test
    public void shouldReturn403IfCartUidIsNotFoundForUpdateCustomerIdRequest(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(12345L, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + UUID.randomUUID().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}