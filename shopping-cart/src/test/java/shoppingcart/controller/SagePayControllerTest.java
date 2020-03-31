package shoppingcart.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

public class SagePayControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "http://localhost:8084/carts/sage";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldDownloadMerchantKey() {
        final HttpEntity<String> payload = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL, HttpMethod.GET, payload, String.class);
        assertThat(response.getBody(), CoreMatchers.containsString("expiry"));
        assertThat(response.getBody(), CoreMatchers.containsString("merchantSessionKey"));
    }

    @Test
    public void shouldSubmitTransactionToSage() {
        // Given
        final UUID uuid = shoppingCartRepository.create(123L, null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.3))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);
        shoppingCartItemRepository.updateQuantity(cart.getId(), "109283", 9);
        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(7.3))
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);
        shoppingCartItemRepository.updateQuantity(cart.getId(), "219283", 6);
        final DeliveryOption deliveryOption = DeliveryOption.builder()
                .method("Express Delivery")
                .charge(1.1d)
                .minDaysRequired(3)
                .maxDaysRequired(5)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addDeliveryOption(cart.getId(), deliveryOption);

        final Address address = new Address();
        address.setAddressType("Shipping");
        address.setTitle("Mr.");
        address.setName("John");
        address.setAddressLine1("10 Kings Road");
        address.setAddressLine2("South Harrow");
        address.setCity("London");
        address.setCountry("United Kingdom");
        address.setPostcode("SH13TG");
        address.setMobile("12345678");
        shoppingCartRepository.addAddress(cart.getId(), address);

        final Address billingAddress = new Address();
        billingAddress.setAddressType("Billing");
        billingAddress.setTitle("Mr.");
        billingAddress.setName("John");
        billingAddress.setAddressLine1("10 Kings Road");
        billingAddress.setAddressLine2("South Harrow");
        billingAddress.setCity("London");
        billingAddress.setCountry("United Kingdom");
        billingAddress.setPostcode("SH13TG");
        billingAddress.setMobile("12345678");
        shoppingCartRepository.addAddress(cart.getId(), billingAddress);

        // When
        final HttpEntity<String> payload = new HttpEntity<>("{\"merchantSessionKey\":\"A54C8070-8BAD-48FA-9775-C530FD4888AA\",\"cardIdentifier\":\"2823DAC5-39D2-4FC6-A1DA-262695174F14\"}", headers);
        final ResponseEntity<String> response = rest.exchange(BASE_URL + "/transactions/" + uuid, HttpMethod.POST, payload, String.class);

        // Then
        assertThat(response.getStatusCode(), CoreMatchers.is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody(), CoreMatchers.containsString("code"));
        assertThat(response.getBody(), CoreMatchers.containsString("description"));

    }

}