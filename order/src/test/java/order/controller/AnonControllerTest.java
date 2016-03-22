package order.controller;


import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
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
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AnonControllerTest extends AbstractControllerTest{

    @Autowired
    private AnonCartRepository anonCartRepository;

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
        final String json = "{\"productId\": \"1\",\"productName\": \"book\",\"productPrice\": \"12.01\",\"quantity\": \"2\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);

        // When
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL, POST, payload, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalCount(), is(1));
        assertThat(response.getBody().getTotalPrice(), is(24.02d));
    }

    @Test
    public void shouldAddExtraItemIntoExistingCart(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10);
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);
        final String json = "{\"cartUid\":\"" +anonCart.getCartUid().toString() +"\",\"productId\": \"1\",\"productName\": \"book\",\"productPrice\": \"12\",\"quantity\": \"2\"}";
        final HttpEntity<String> payload = new HttpEntity<String>(json, headers);

        // When
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL, POST, payload, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), is(anonCart.getCartUid()));
        assertThat(response.getBody().getTotalCount(), is(2));
        assertThat(response.getBody().getTotalPrice(), is(34d));
    }

    @Test
    public void shouldGetCartSummaryByCartUid(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10);
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/" + anonCart.getCartUid().toString(), GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalCount(), is(1));
        assertThat(response.getBody().getTotalPrice(), is(10D));
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
        final AnonCartItem firstCartItem = new AnonCartItem(2, "pen", 1, 11);
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.setCustomerId(12345l);
        anonCartRepository.save(anonCart);

        // When
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/?customerId=12345" , GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalCount(), is(1));
        assertThat(response.getBody().getTotalPrice(), is(11D));
    }

    @Test
    public void shouldReturn404IfNoCartSummaryIsFoundByCustomerId(){
        // Given & When
        final Random random = new Random();
        final long customerId = random.nextLong();
        final HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/?customerId="+customerId , GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldUpdateCustomerIdAndDeleteOtherCartsForTheSameCustomer(){
        // Given
        final Long customerId = 102534l;

        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1, 10);
        anonCart.addAnonCartItem(firstCartItem);
        anonCartRepository.save(anonCart);

        final AnonCart otherCart = new AnonCart();
        otherCart.setCustomerId(customerId);
        final AnonCartItem otherCartItem = new AnonCartItem(1, "book", 1, 10);
        otherCart.addAnonCartItem(otherCartItem);
        anonCartRepository.save(otherCart);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(customerId, headers);
        ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + anonCart.getCartUid().toString(), HttpMethod.PUT, payload, Void.class);

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
        ResponseEntity<Void> response = rest.exchange(BASE_URL + "/" + randomUUID().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

}
