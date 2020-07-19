package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import shoppingcart.data.CartData;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

public class ShoppingCartItemControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ShoppingCartItemRepository itemRepository;

    @Test
    public void shouldAddItemToCart(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String json = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"10.99\",\n" +
                "\"imageUrl\": \"/cloth.jpeg\",\n" +
                "\"description\": \"Size: M\",\n" +
                "\"vatRate\": 20,\n" +
                "\"code\": \"code\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        ResponseEntity<CartData> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody().getQuantity(), is(1));
        assertThat(response.getBody().getOrderTotal(), is(BigDecimal.valueOf(10.99).setScale(2, ROUND_HALF_UP)));
        assertThat(response.getBody().getCartItems().size(), is(1));
        assertThat(response.getBody().getCartItems().get(0).getSku(), is("123456"));
        assertThat(response.getBody().getCartItems().get(0).getName(), is("kid's cloth"));
        assertThat(response.getBody().getCartItems().get(0).getPrice(), is(BigDecimal.valueOf(10.99).setScale(2, ROUND_HALF_UP)));
        assertThat(response.getBody().getCartItems().get(0).getThumbnail(), is("/cloth.jpeg"));
        assertThat(response.getBody().getCartItems().get(0).getQuantity(), is(1));
        assertThat(response.getBody().getCartItems().get(0).getDescription(), is("Size: M"));
        assertThat(response.getBody().getCartItems().get(0).getCode(), is("code"));
        assertThat(response.getBody().getCartItems().get(0).getVatRate(), is(20));
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
        ResponseEntity<CartData> response = rest.exchange(BASE_URL + randomUUID().toString() + "/items", POST, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
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
                "\"vatRate\": 20,\n" +
                "\"imageUrl\": \"/cloth.jpeg\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartData.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartData.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartData.class);
        ResponseEntity<CartData> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody().getQuantity(), is(4));
        assertThat(response.getBody().getOrderTotal(), is(BigDecimal.valueOf(43.96)));
        assertThat(response.getBody().getCartItems().size(), is(1));
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final List<ShoppingCartItem> cartItems = itemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getSku(), is("123456"));
        assertThat(cartItems.get(0).getName(), is("kid's cloth"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(10.99)));
        assertThat(cartItems.get(0).getImageUrl(), is("/cloth.jpeg"));
        assertThat(cartItems.get(0).getQuantity(), is(4));
        assertThat(cartItems.get(0).getVatRate(), is(20));
    }

    @Test
    public void shouldBeAbleToAddDifferentItemsToCart(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String itemOne = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemOnePayload = new HttpEntity<>(itemOne, headers);

        // When & Then
        final ResponseEntity<CartData> cartSummaryOne = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartData.class);
        assertThat(cartSummaryOne.getStatusCode(), is(CREATED));
        assertThat(cartSummaryOne.getBody().getQuantity(), is(1));
        assertThat(cartSummaryOne.getBody().getOrderTotal(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(cartSummaryOne.getBody().getCartItems().size(), is(1));

        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemTwoPayload = new HttpEntity<>(itemTwo, headers);
        final ResponseEntity<CartData> cartSummaryTwo = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);
        assertThat(cartSummaryTwo.getStatusCode(), is(CREATED));
        assertThat(cartSummaryTwo.getBody().getQuantity(), is(2));
        assertThat(cartSummaryTwo.getBody().getOrderTotal(), is(BigDecimal.valueOf(11).setScale(2, ROUND_HALF_UP)));
        assertThat(cartSummaryTwo.getBody().getCartItems().size(), is(2));
    }

    @Test
    public void shouldDeleteItemFromCart(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String itemOne = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemOnePayload = new HttpEntity<>(itemOne, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartData.class);

        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemTwoPayload = new HttpEntity<>(itemTwo, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartData.class);

        // When & Then
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<CartData> cartSummaryOne = rest.exchange(BASE_URL + cartUid.toString() + "/items/123456", DELETE, httpEntity, CartData.class);
        assertThat(cartSummaryOne.getStatusCode(), is(OK));
        assertThat(cartSummaryOne.getBody().getQuantity(), is(1));
        assertThat(cartSummaryOne.getBody().getOrderTotal(), is(BigDecimal.valueOf(10).setScale(2, ROUND_HALF_UP)));
        assertThat(cartSummaryOne.getBody().getCartItems().size(), is(1));

        final ResponseEntity<CartData> cartSummaryTwo = rest.exchange(BASE_URL + cartUid.toString() + "/items/654321", DELETE, httpEntity, CartData.class);
        assertThat(cartSummaryTwo.getStatusCode(), is(OK));
        assertThat(cartSummaryTwo.getBody().getQuantity(), is(0));
        assertThat(cartSummaryTwo.getBody().getOrderTotal(), is(BigDecimal.valueOf(0).setScale(2, ROUND_HALF_UP)));
        assertThat(cartSummaryTwo.getBody().getCartItems().size(), is(0));
    }

    @Test
    public void shouldUpdateCartItemQuantity(){
        // Given
        final UUID cartUid = cartRepository.create();

        final String item = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemPayload = new HttpEntity<>(item, headers);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemPayload, CartData.class);

        // When
        final String updatedItem = "{\n" +
                "\"sku\": \"123456\",\n" +
                "\"name\": \"kid's cloth\",\n" +
                "\"price\": \"1\",\n" +
                "\"imageUrl\": \"/kid-cloth.jpeg\",\n" +
                "\"quantity\": \"10\"\n" +
                "}";
        final HttpEntity<String> updatedItemPayload = new HttpEntity<>(updatedItem, headers);
        ResponseEntity<Void> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", PUT, updatedItemPayload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final List<ShoppingCartItem> cartItems = itemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getSku(), is("123456"));
        assertThat(cartItems.get(0).getName(), is("kid's cloth"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(cartItems.get(0).getImageUrl(), is("/kid-cloth.jpeg"));
        assertThat(cartItems.get(0).getQuantity(), is(10));
    }
}