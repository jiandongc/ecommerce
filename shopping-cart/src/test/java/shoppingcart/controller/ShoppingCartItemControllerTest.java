package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import shoppingcart.data.CartSummary;
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
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
                "\"imageUrl\": \"/cloth.jpeg\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);
        ResponseEntity<CartSummary> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartSummary.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody().getTotalQuantity(), is(1));
        assertThat(response.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(10.99)));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().size(), is(1));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().get(0).getSku(), is("123456"));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().get(0).getName(), is("kid's cloth"));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().get(0).getPrice(), is(BigDecimal.valueOf(10.99)));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().get(0).getImageUrl(), is("/cloth.jpeg"));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().get(0).getQuantity(), is(1));
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
        ResponseEntity<CartSummary> response = rest.exchange(BASE_URL + randomUUID().toString() + "/items", POST, payload, CartSummary.class);

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
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartSummary.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartSummary.class);
        rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartSummary.class);
        ResponseEntity<CartSummary> response = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, payload, CartSummary.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        assertThat(response.getBody().getTotalQuantity(), is(4));
        assertThat(response.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(43.96)));
        assertThat(response.getBody().getShoppingCart().getShoppingCartItems().size(), is(1));
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final List<ShoppingCartItem> cartItems = itemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getSku(), is("123456"));
        assertThat(cartItems.get(0).getName(), is("kid's cloth"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(10.99)));
        assertThat(cartItems.get(0).getImageUrl(), is("/cloth.jpeg"));
        assertThat(cartItems.get(0).getQuantity(), is(4));
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

        final HttpEntity<String> itemOnePayload = new HttpEntity<String>(itemOne, headers);

        // When & Then
        final ResponseEntity<CartSummary> cartSummaryOne = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemOnePayload, CartSummary.class);
        assertThat(cartSummaryOne.getStatusCode(), is(CREATED));
        assertThat(cartSummaryOne.getBody().getTotalQuantity(), is(1));
        assertThat(cartSummaryOne.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(1)));
        assertThat(cartSummaryOne.getBody().getShoppingCart().getShoppingCartItems().size(), is(1));

        final String itemTwo = "{\n" +
                "\"sku\": \"654321\",\n" +
                "\"name\": \"father's cloth\",\n" +
                "\"price\": \"10\",\n" +
                "\"imageUrl\": \"/father-cloth.jpeg\"\n" +
                "}";

        final HttpEntity<String> itemTwoPayload = new HttpEntity<String>(itemTwo, headers);
        final ResponseEntity<CartSummary> cartSummaryTwo = rest.exchange(BASE_URL + cartUid.toString() + "/items", POST, itemTwoPayload, CartSummary.class);
        assertThat(cartSummaryTwo.getStatusCode(), is(CREATED));
        assertThat(cartSummaryTwo.getBody().getTotalQuantity(), is(2));
        assertThat(cartSummaryTwo.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(11)));
        assertThat(cartSummaryTwo.getBody().getShoppingCart().getShoppingCartItems().size(), is(2));
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

        // When & Then
        final HttpEntity<String> skuOne = new HttpEntity<String>("123456", headers);
        final ResponseEntity<CartSummary> cartSummaryOne = rest.exchange(BASE_URL + cartUid.toString() + "/items", DELETE, skuOne, CartSummary.class);
        assertThat(cartSummaryOne.getStatusCode(), is(OK));
        assertThat(cartSummaryOne.getBody().getTotalQuantity(), is(1));
        assertThat(cartSummaryOne.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(10)));
        assertThat(cartSummaryOne.getBody().getShoppingCart().getShoppingCartItems().size(), is(1));

        final HttpEntity<String> skuTwo = new HttpEntity<String>("654321", headers);
        final ResponseEntity<CartSummary> cartSummaryTwo = rest.exchange(BASE_URL + cartUid.toString() + "/items", DELETE, skuTwo, CartSummary.class);
        assertThat(cartSummaryTwo.getStatusCode(), is(OK));
        assertThat(cartSummaryTwo.getBody().getTotalQuantity(), is(0));
        assertThat(cartSummaryTwo.getBody().getItemsSubTotal(), is(BigDecimal.valueOf(0)));
        assertThat(cartSummaryTwo.getBody().getShoppingCart().getShoppingCartItems().size(), is(0));

    }



}