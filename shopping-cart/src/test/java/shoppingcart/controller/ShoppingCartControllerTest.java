package shoppingcart.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import shoppingcart.data.CartData;
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
                "\"imageUrl\": \"/kid-cloth.jpeg\",\n" +
                "\"description\": \"Size: S\"\n" +
                "}";
        final HttpEntity<String> itemOnePayload = new HttpEntity<String>(itemOne, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartData.class);
        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\",\n" +
                "\"description\": \"Size: M\"\n" +
                "}";
        final HttpEntity<String> itemTwoPayload = new HttpEntity<String>(itemTwo, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + cartUid.toString(), GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getQuantity(), is(3));
        assertThat(response.getBody().getSubTotal().compareTo(BigDecimal.valueOf(21)), is(0));
        assertThat(response.getBody().getCartItems().size(), is(2));
        assertThat(response.getBody().getCartItems().get(0).getSku(), is("123456"));
        assertThat(response.getBody().getCartItems().get(0).getDescription(), is("Size: S"));
        assertThat(response.getBody().getCartItems().get(1).getSku(), is("654321"));
        assertThat(response.getBody().getCartItems().get(1).getDescription(), is("Size: M"));
    }

    @Test
    public void shouldReturn404IfCartUidIsNotValid(){
        // Given
        final UUID cartUid = UUID.randomUUID();

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + cartUid.toString(), GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldRetrieveShoppingCartByCustomerID(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final Long customerId = 100L;
        final UUID cartUid = repository.create(customerId);
        final String itemOne = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\",\n" +
                "\"description\": \"Size: S\"\n" +
                "}";
        final HttpEntity<String> itemOnePayload = new HttpEntity<String>(itemOne, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartData.class);
        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\",\n" +
                "\"description\": \"Size: M\"\n" +
                "}";
        final HttpEntity<String> itemTwoPayload = new HttpEntity<String>(itemTwo, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=100", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getQuantity(), is(3));
        assertThat(response.getBody().getSubTotal(), is(BigDecimal.valueOf(21)));
        assertThat(response.getBody().getCartItems().size(), is(2));
        assertThat(response.getBody().getCartItems().get(0).getSku(), is("123456"));
        assertThat(response.getBody().getCartItems().get(0).getDescription(), is("Size: S"));
        assertThat(response.getBody().getCartItems().get(1).getSku(), is("654321"));
        assertThat(response.getBody().getCartItems().get(1).getDescription(), is("Size: M"));
    }

    @Test
    public void shouldReturn404IfNoShoppingCartIsFoundUsingCustomerId(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=999", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldRejectRetrieveShoppingCartByCustomerIdRequestWithGuestToken(){
        // Given - set guest token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        repository.create(100L);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=100", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldUpdateCustomerUidForTheCartUidAndDeleteOtherCarts(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final Long customerId = 12345L;
        final UUID cartUidOne = repository.create(customerId);
        final UUID cartUidTwo = repository.create(customerId);
        final UUID cartUidThree = repository.create(customerId);
        assertThat(repository.findByUUID(cartUidOne).isPresent(), is(true));
        assertThat(repository.findByUUID(cartUidTwo).isPresent(), is(true));
        assertThat(repository.findByUUID(cartUidThree).isPresent(), is(true));

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(12345L, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + cartUidOne.toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repository.findByUUID(cartUidOne).get().getCustomerId(), is(12345L));
        assertThat(repository.findByUUID(cartUidTwo).isPresent(), is(false));
        assertThat(repository.findByUUID(cartUidThree).isPresent(), is(false));
    }

    @Test
    public void shouldRejectUpdateCustomerUidRequestWithGuestToken(){
        // Given - set guest token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        final UUID cartUid = repository.create();

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(12345L, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + cartUid.toString(), HttpMethod.PUT, payload, Void.class);

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
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + UUID.randomUUID().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}