package shoppingcart.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartRepository;
import shoppingcart.service.ShoppingCartEmailService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;

public class AdminControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ShoppingCartRepository repository;

    @Autowired
    private ShoppingCartEmailService shoppingCartEmailService;

    @Test
    public void shouldFindAllShoppingCarts() {
        // Given - set Admin Token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        UUID uuidOne = repository.create();
        UUID uuidTwo = repository.create();
        UUID uuidThree = repository.create();

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/admin/carts", GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), uuidOne.toString()), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), uuidTwo.toString()), is(1));
        assertThat(StringUtils.countOccurrencesOf(response.getBody(), uuidThree.toString()), is(1));
    }

    @Test
    public void shouldReturnShoppingCartCreatedBeforeDate() {
        // Given - set Admin Token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        repository.create();
        repository.create();
        repository.create();

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/admin/carts?date=2100-01-01", GET, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("[]"));
    }

    @Test
    public void shouldDeleteShoppingCart() {
        // Given - set Admin Token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi5vbmVAbm9vZGxlLW1vbnN0ZXIuY28udWsiLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiXSwiZXhwIjo0MTk0NTM4NzE1fQ.9gV7YH_1w0sDlUp-7YIHAvkBa7t4MzT6qq0Ijs0rgfi9ET-Hwm-g9euUiNm4wdCzw1CWVoazlsXxYOkE8yBHkw");
        UUID uuid = repository.create();
        assertThat(repository.findByUUID(uuid).isPresent(), is(true));

        // When
        final HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("/admin/carts/" + uuid.toString(), DELETE, httpEntity, String.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repository.findByUUID(uuid).isPresent(), is(false));
    }
}