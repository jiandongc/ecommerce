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
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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
        assertThat(response.getBody().getTotalPrice(), is(24.02D));
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
        assertThat(response.getBody().getTotalPrice(), is(34D));
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
        final ResponseEntity<CartSummaryData> response = rest.exchange(BASE_URL + "/summary/" + anonCart.getCartUid().toString(), HttpMethod.GET, httpEntity, CartSummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCartUid(), instanceOf(UUID.class));
        assertThat(response.getBody().getTotalCount(), is(1));
        assertThat(response.getBody().getTotalPrice(), is(10D));
    }
}
