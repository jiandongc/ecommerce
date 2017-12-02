package shoppingcart.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ShoppingCartControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/";
    private final TestRestTemplate rest = new TestRestTemplate();
    private HttpHeaders headers;

    @Before
    public void before() {
        this.reset();
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(APPLICATION_JSON);
        }
    }

    @After
    public void cleanup(){
        this.reset();
    }

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
        final ShoppingCart shoppingCart = repository.findByUUID(uuid);
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
        final ShoppingCart shoppingCart = repository.findByUUID(uuid);
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerId(), is(nullValue()));
    }


}