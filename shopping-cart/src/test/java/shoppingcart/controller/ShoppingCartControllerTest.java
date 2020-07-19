package shoppingcart.controller;

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
        // Given
        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-12d3-a456-556642440000\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";

        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);

        // When
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final UUID uuid = UUID.fromString(response.getBody());
        final ShoppingCart shoppingCart = repository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
        assertThat(shoppingCart.getEmail(), is("joe@gmail.com"));
    }

    @Test
    public void shouldCreateShoppingCartForUserWithNoEmail(){
        // Given
        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-12d3-a456-556642440000\"\n" +
                "}";

        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);

        // When
        final ResponseEntity<String> response = rest.exchange(BASE_URL, POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), is(CREATED));
        final UUID uuid = UUID.fromString(response.getBody());
        final ShoppingCart shoppingCart = repository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
        assertThat(shoppingCart.getEmail(), is(nullValue()));
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
        assertThat(shoppingCart.getCustomerUid(), is(nullValue()));
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
        assertThat(response.getStatusCode(), is(FORBIDDEN));
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
        assertThat(response.getBody().getItemsTotal(), is(BigDecimal.valueOf(21).setScale(2)));
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
        final String customerId = "123e4567-e89b-12d3-a456-556642440000";
        final UUID cartUid = repository.create(customerId, null);
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
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=123e4567-e89b-12d3-a456-556642440000", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(OK));
        assertThat(response.getBody().getQuantity(), is(3));
        assertThat(response.getBody().getItemsTotal(), is(BigDecimal.valueOf(21).setScale(2)));
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
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=123e4567-e89b-12d3-a456-556642440000", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldRejectRetrieveShoppingCartByCustomerIdRequestWithGuestToken(){
        // Given - set guest token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        repository.create("123e4567-e89b-12d3-a456-556642440000", null);

        // When
        final HttpEntity<Long> payload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + "?customerId=100", GET, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test
    public void shouldUpdateCustomerInfoForTheCartUidAndDeleteOtherCarts(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final String customerId = "123e4567-e89b-12d3-a456-556642440000";
        final UUID cartUidOne = repository.create(customerId, null);
        final UUID cartUidTwo = repository.create(customerId, null);
        final UUID cartUidThree = repository.create(customerId, null);
        assertThat(repository.findByUUID(cartUidOne).isPresent(), is(true));
        assertThat(repository.findByUUID(cartUidTwo).isPresent(), is(true));
        assertThat(repository.findByUUID(cartUidThree).isPresent(), is(true));

        final String customerData = "{\n" +
                "\"id\": \"123e4567-e89b-12d3-a456-556642440000\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + cartUidOne.toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(repository.findByUUID(cartUidOne).get().isActive(), is(true));
        assertThat(repository.findByUUID(cartUidOne).get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
        assertThat(repository.findByUUID(cartUidOne).get().getEmail(), is("joe@gmail.com"));
        assertThat(repository.findByUUID(cartUidTwo).isPresent(), is(false));
        assertThat(repository.findByUUID(cartUidThree).isPresent(), is(false));

        // When
        final HttpEntity<Long> emptyPayload = new HttpEntity<Long>(null, headers);
        final ResponseEntity<CartData> responseOne = rest.exchange(BASE_URL + cartUidOne.toString(), GET, emptyPayload, CartData.class);
        assertThat(responseOne.getStatusCode(), is(HttpStatus.OK));

        final ResponseEntity<CartData> responseTwo = rest.exchange(BASE_URL + cartUidTwo.toString(), GET, emptyPayload, CartData.class);
        assertThat(responseTwo.getStatusCode(), is(HttpStatus.NOT_FOUND));

        final ResponseEntity<CartData> responseThree = rest.exchange(BASE_URL + cartUidTwo.toString(), GET, emptyPayload, CartData.class);
        assertThat(responseThree.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldUpdateCartEmailOnly(){
        // Given - set guest token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        final UUID cartUid = repository.create();
        assertThat(repository.findByUUID(cartUid).isPresent(), is(true));
        final String customerData = "{\n" +
                "\"email\": \"tom@gmail.com\"\n" +
                "}";

        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);
        final ResponseEntity<CartData> response = rest.exchange(BASE_URL + cartUid.toString(), HttpMethod.PUT, payload, CartData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getEmail(), is("tom@gmail.com"));
        assertThat(repository.findByUUID(cartUid).get().getEmail(), is("tom@gmail.com"));
    }


    @Test
    public void shouldReturn403IfCartUidIsNotFoundForUpdateCustomerIdRequest(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final String customerData = "{\n" +
                "\"customerId\": \"12345\",\n" +
                "\"email\": \"joe@gmail.com\"\n" +
                "}";
        // When
        final HttpEntity<String> payload = new HttpEntity<>(customerData, headers);
        final ResponseEntity<Void> response = rest.exchange(BASE_URL + UUID.randomUUID().toString(), HttpMethod.PUT, payload, Void.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void shouldAddAndUpdateAddress(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final String customerId = "123e4567-e89b-12d3-a456-556642440000";
        final UUID cartUid = repository.create(customerId, null);

        // When - add shipping address
        final String shipping = "{\n" +
                "\"addressType\": \"Shipping\",\n" +
                "\"name\": \"John\",\n" +
                "\"title\": \"Mr.\",\n" +
                "\"mobile\": \"12345678\",\n" +
                "\"addressLine1\": \"addressLine1\",\n" +
                "\"addressLine2\": \"addressLine2\",\n" +
                "\"addressLine3\": \"addressLine3\",\n" +
                "\"city\": \"London\",\n" +
                "\"country\": \"UK\",\n" +
                "\"postcode\": \"EF1 8HD\"\n" +
                "}";
        final HttpEntity<String> shippingPayload = new HttpEntity<>(shipping, headers);
        final ResponseEntity<CartData> shippingResponse = rest.exchange(BASE_URL + cartUid.toString() + "/addresses", POST, shippingPayload, CartData.class);

        // Then
        assertThat(shippingResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(shippingResponse.getBody().getShipping().getName(), is("John"));
        assertThat(shippingResponse.getBody().getShipping().getPostcode(), is("EF1 8HD"));

        // When - add billing address
        final String billing = "{\n" +
                "\"addressType\": \"Billing\",\n" +
                "\"name\": \"Mary\",\n" +
                "\"title\": \"Mr.\",\n" +
                "\"mobile\": \"12345678\",\n" +
                "\"addressLine1\": \"addressLine1\",\n" +
                "\"addressLine2\": \"addressLine2\",\n" +
                "\"addressLine3\": \"addressLine3\",\n" +
                "\"city\": \"London\",\n" +
                "\"country\": \"UK\",\n" +
                "\"postcode\": \"EF2 8HD\"\n" +
                "}";
        final HttpEntity<String> billingPayload = new HttpEntity<>(billing, headers);
        final ResponseEntity<CartData> billingResponse = rest.exchange(BASE_URL + cartUid.toString() + "/addresses", POST, billingPayload, CartData.class);

        // Then
        assertThat(billingResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(billingResponse.getBody().getBilling().getName(), is("Mary"));
        assertThat(billingResponse.getBody().getBilling().getPostcode(), is("EF2 8HD"));

        // When - update billing address
        final String billingUpdate = "{\n" +
                "\"addressType\": \"Billing\",\n" +
                "\"name\": \"Conor\",\n" +
                "\"title\": \"Mr.\",\n" +
                "\"mobile\": \"12345678\",\n" +
                "\"addressLine1\": \"addressLine1\",\n" +
                "\"addressLine2\": \"addressLine2\",\n" +
                "\"addressLine3\": \"addressLine3\",\n" +
                "\"city\": \"London\",\n" +
                "\"country\": \"UK\",\n" +
                "\"postcode\": \"EF3 8HD\"\n" +
                "}";
        final HttpEntity<String> billingUpdatePayload = new HttpEntity<>(billingUpdate, headers);
        final ResponseEntity<CartData> billingUpdatePayloadResponse = rest.exchange(BASE_URL + cartUid.toString() + "/addresses", POST, billingUpdatePayload, CartData.class);

        // Then
        assertThat(billingUpdatePayloadResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(billingUpdatePayloadResponse.getBody().getBilling().getName(), is("Conor"));
        assertThat(billingUpdatePayloadResponse.getBody().getBilling().getPostcode(), is("EF3 8HD"));
    }

    @Test
    public void shouldAddDeliveryOption(){
        // Given - set user token
        headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        final String customerId = "123e4567-e89b-12d3-a456-556642440000";
        final UUID cartUid = repository.create(customerId, null);

        // When - add delivery option
        final String deliveryOption = "{\n" +
                "\"method\": \"FREE Delivery\",\n" +
                "\"charge\": \"3.0\",\n" +
                "\"minDaysRequired\": \"1\",\n" +
                "\"maxDaysRequired\": \"3\",\n" +
                "\"vatRate\": 20\n" +
                "}";
        final HttpEntity<String> deliveryOptionPayload = new HttpEntity<>(deliveryOption, headers);
        final ResponseEntity<CartData> cartDataResponseEntity = rest.exchange(BASE_URL + cartUid.toString() + "/deliveryoption", POST, deliveryOptionPayload, CartData.class);

        // Then
        assertThat(cartDataResponseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(cartDataResponseEntity.getBody().getCartUid(), is(cartUid.toString()));
        assertThat(cartDataResponseEntity.getBody().getDeliveryOption().getMethod(), is("FREE Delivery"));
        assertThat(cartDataResponseEntity.getBody().getDeliveryOption().getCharge(), is(3D));
        assertThat(cartDataResponseEntity.getBody().getDeliveryOption().getMinDaysRequired(), is(1));
        assertThat(cartDataResponseEntity.getBody().getDeliveryOption().getMaxDaysRequired(), is(3));
        assertThat(cartDataResponseEntity.getBody().getDeliveryOption().getVatRate(), is(20));

        // When - update delivery option
        final String deliveryOptionUpdate = "{\n" +
                "\"method\": \"Express Delivery\",\n" +
                "\"charge\": \"5.0\",\n" +
                "\"minDaysRequired\": \"1\",\n" +
                "\"maxDaysRequired\": \"2\",\n" +
                "\"vatRate\": 10\n" +
                "}";
        final HttpEntity<String> deliveryOptionUpdatePayload = new HttpEntity<>(deliveryOptionUpdate, headers);
        final ResponseEntity<CartData> cartDataUpdateResponseEntity = rest.exchange(BASE_URL + cartUid.toString() + "/deliveryoption", POST, deliveryOptionUpdatePayload, CartData.class);

        // Then
        assertThat(cartDataUpdateResponseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(cartDataUpdateResponseEntity.getBody().getCartUid(), is(cartUid.toString()));
        assertThat(cartDataUpdateResponseEntity.getBody().getDeliveryOption().getMethod(), is("Express Delivery"));
        assertThat(cartDataUpdateResponseEntity.getBody().getDeliveryOption().getCharge(), is(5D));
        assertThat(cartDataUpdateResponseEntity.getBody().getDeliveryOption().getMinDaysRequired(), is(1));
        assertThat(cartDataUpdateResponseEntity.getBody().getDeliveryOption().getMaxDaysRequired(), is(2));
        assertThat(cartDataUpdateResponseEntity.getBody().getDeliveryOption().getVatRate(), is(10));
    }

}