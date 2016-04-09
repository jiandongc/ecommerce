package order.controller;


import order.data.AnonCartItemData;
import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import order.service.AnonCartService;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AnonControllerTest extends AbstractControllerTest {

    @Autowired
    private AnonCartRepository anonCartRepository;
    @Autowired
    private AnonCartService anonCartService;

    private final String BASE_URL = "http://localhost:8082/anoncarts";
    private final RestTemplate rest = new TestRestTemplate();
    private HttpHeaders headers = null;

    @Before
    public void before(){
        if (headers == null) {
            headers = new HttpHeaders();
            final String auth = "test:password";
            final byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            final String authHeader = "Basic " + new String(encodedAuth);
            headers.add("Authorization", authHeader);
            headers.setContentType(APPLICATION_JSON);
        }
    }

    @After
    public void after(){
        anonCartRepository.deleteAll();
    }

    @Test
    public void shouldSaveAnonCartWhenFirstCartItemIsAdded(){
        // Given
        final String json = "{\"productId\": \"1\",\"productName\": \"book\",\"productPrice\": \"12.01\",\"quantity\": \"2\",\"imageUrl\": \"http://book.jpeg\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);

        // When
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL, POST, payload, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalQuantity(), is(2));
        assertThat(response.getBody().getTotalPrice(), is(24.02d));

        assertThat(response.getBody().getCartItems().size(), is(1));
        final Set<AnonCartItemData> cartItems = response.getBody().getCartItems();
        final AnonCartItemData anonCartItemData = cartItems.iterator().next();
        assertThat(anonCartItemData.getCartUid(), instanceOf(UUID.class));
        assertThat(anonCartItemData.getProductId(), is(1l));
        assertThat(anonCartItemData.getProductName(), is("book"));
        assertThat(anonCartItemData.getProductPrice(), is(12.01d));
        assertThat(anonCartItemData.getQuantity(), is(2));
        assertThat(anonCartItemData.getSubTotal(), is(24.02d));
        assertThat(anonCartItemData.getImageUrl(), is("http://book.jpeg"));
    }

    @Test
    public void shouldAddExtraItemIntoExistingCart(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10, "http://book.jpeg");
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);
        final String json = "{\"cartUid\":\"" +anonCart.getCartUid().toString() +"\",\"productId\": \"2\",\"productName\": \"pen\",\"productPrice\": \"12\",\"quantity\": \"2\",\"imageUrl\": \"http://pen.jpeg\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);

        // When
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL, POST, payload, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), is(anonCart.getCartUid()));
        assertThat(response.getBody().getTotalQuantity(), is(12));
        assertThat(response.getBody().getTotalPrice(), is(34d));
        assertThat(response.getBody().getCartItems().size(), is(2));
    }

    @Test
    public void shouldGetCartSummaryByCartUid(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10, "http://book.jpeg");
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/" + anonCart.getCartUid().toString(), GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalQuantity(), is(10));
        assertThat(response.getBody().getTotalPrice(), is(10D));

        assertThat(response.getBody().getCartItems().size(), is(1));
        final Set<AnonCartItemData> cartItems = response.getBody().getCartItems();
        final AnonCartItemData anonCartItemData = cartItems.iterator().next();
        assertThat(anonCartItemData.getCartUid(), instanceOf(UUID.class));
        assertThat(anonCartItemData.getProductId(), is(1l));
        assertThat(anonCartItemData.getProductName(), is("book"));
        assertThat(anonCartItemData.getProductPrice(), is(1d));
        assertThat(anonCartItemData.getQuantity(), is(10));
        assertThat(anonCartItemData.getSubTotal(), is(10d));
        assertThat(anonCartItemData.getImageUrl(), is("http://book.jpeg"));

    }

    @Test
    public void shouldReturn404IfNoCartSummaryIsFoundByCartUid(){
        // Given & When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/" + randomUUID().toString(), GET, httpEntity, CartSummaryData.class);
        
        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldGetCartSummaryByCustomerId(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(2, "pen", 1, 11, "http://pen.jpeg");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.setCustomerId(12345l);
        anonCartRepository.save(anonCart);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/?customerId=12345" , GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalQuantity(), is(11));
        assertThat(response.getBody().getTotalPrice(), is(11D));

        assertThat(response.getBody().getCartItems().size(), is(1));
        final Set<AnonCartItemData> cartItems = response.getBody().getCartItems();
        final AnonCartItemData anonCartItemData = cartItems.iterator().next();
        assertThat(anonCartItemData.getCartUid(), instanceOf(UUID.class));
        assertThat(anonCartItemData.getProductId(), is(2l));
        assertThat(anonCartItemData.getProductName(), is("pen"));
        assertThat(anonCartItemData.getProductPrice(), is(1d));
        assertThat(anonCartItemData.getQuantity(), is(11));
        assertThat(anonCartItemData.getSubTotal(), is(11d));
        assertThat(anonCartItemData.getImageUrl(), is("http://pen.jpeg"));
    }

    @Test
    public void shouldReturn404IfNoCartSummaryIsFoundByCustomerId(){
        // Given & When
        final Random random = new Random();
        final long customerId = random.nextLong();
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/?customerId=" + customerId , GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldUpdateCustomerIdAndDeleteOtherCartsForTheSameCustomer(){
        // Given
        final Long customerId = 102534l;

        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10, "http://book.jpeg");
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);

        final AnonCart otherCart = new AnonCart();
        otherCart.setCustomerId(customerId);
        final AnonCartItem otherCartItem = new AnonCartItem(1, "book", 1, 10, "http://book.jpeg");
        otherCart.addAnonCartItem(otherCartItem);
        anonCartRepository.save(otherCart);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(customerId, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + anonCart.getCartUid().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        final AnonCart actualAnonCart = anonCartRepository.findByCartUid(anonCart.getCartUid());
        assertThat(actualAnonCart.getCustomerId(), is(customerId));
        final AnonCart actualOtherCart = anonCartRepository.findByCartUid(otherCart.getCartUid());
        assertThat(actualOtherCart, is(nullValue()));
    }

    @Test
    public void shouldReturn404IfCustomerIsNotFound(){
        // Given & When
        final Long customerId = 102534l;
        final HttpEntity<Long> payload = new HttpEntity<Long>(customerId, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + randomUUID().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldDeleteCartItemByCartUidAndProductId(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        final AnonCartItem secondCartItem = new AnonCartItem(2, "pen", 1.2, 20, "url");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.addAnonCartItem(secondCartItem);
        anonCartRepository.save(anonCart);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + anonCart.getCartUid().toString() + "/?productId=" + firstCartItem.getProductId(), HttpMethod.DELETE, httpEntity, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
        final AnonCart actualAnonCart = anonCartService.findAnonCartByUidForTest(anonCart.getCartUid());
        assertThat(actualAnonCart.getTotalPrice(), is(24d));
        assertThat(actualAnonCart.getTotalQuantity(), is(20));
        assertThat(actualAnonCart.getAnonCartItems().size(), is(1));
        assertThat(actualAnonCart.getAnonCartItems().iterator().next(), is(secondCartItem));

        // When - repeat the delete again, delete operation should be idempotent
        final HttpEntity<?> httpEntity2 = new HttpEntity<Object>(headers);
        final ResponseEntity<Void> response2 = rest.exchange(BASE_URL + "/" + anonCart.getCartUid().toString() + "/?productId=" + firstCartItem.getProductId(), HttpMethod.DELETE, httpEntity2, Void.class);

        // Then
        assertThat(response2.getStatusCode(), is(HttpStatus.NO_CONTENT));
        final AnonCart actualAnonCart2 = anonCartService.findAnonCartByUidForTest(anonCart.getCartUid());
        assertThat(actualAnonCart2.getTotalPrice(), is(24d));
        assertThat(actualAnonCart2.getTotalQuantity(), is(20));
        assertThat(actualAnonCart2.getAnonCartItems().size(), is(1));
        assertThat(actualAnonCart2.getAnonCartItems().iterator().next(), is(secondCartItem));
    }

    @Test
    public void shouldReturnNoContentWhatCartUidIsNotFound(){
        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + randomUUID().toString() + "/?productId=12345", HttpMethod.DELETE, httpEntity, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NO_CONTENT));
    }

}
