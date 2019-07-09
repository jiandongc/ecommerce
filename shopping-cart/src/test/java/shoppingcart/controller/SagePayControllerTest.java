package shoppingcart.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;

public class SagePayControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/sage";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void shouldDownloadMerchantKey(){
        final HttpEntity<String> payload = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, HttpMethod.GET, payload, String.class);
        assertThat(response.getBody(), CoreMatchers.containsString("expiry"));
        assertThat(response.getBody(), CoreMatchers.containsString("merchantSessionKey"));
    }

}