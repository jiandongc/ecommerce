package shoppingcart.repository;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.Promotion;
import shoppingcart.domain.ShoppingCart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ShoppingCartRepositoryImplTest extends AbstractRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldCreateShoppingCartWithoutCustomerId() throws Exception {
        // Given & When
        final UUID uuid = shoppingCartRepository.create();

        // Then
        final ShoppingCart shoppingCart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
    }

    @Test
    public void shouldCreateShoppingCartWithCustomerId() throws Exception {
        // Given & When
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);

        // Then
        final ShoppingCart shoppingCart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
    }

    @Test
    public void shouldReturnEmptyOptionalIfCartUidIsNotFound(){
        // Given & When
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUUID(UUID.randomUUID());

        // Then
        assertThat(cart.isPresent(), is(false));
    }

    @Test
    public void shouldUpdateShoppingCartCustomerUid(){
        // Given
        final UUID uuid = shoppingCartRepository.create();

        // When
        int rowUpdated = shoppingCartRepository.updateCustomerUid(uuid, UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));

        // Then
        assertThat(rowUpdated, is(1));
        assertThat(shoppingCartRepository.findByUUID(uuid).get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
    }

    @Test
    public void shouldReturnShoppingCartByCustomerId(){
        // Given
        shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);

        // When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));

        // Then
        assertThat(shoppingCarts.size(), is(2));
    }

    @Test
    public void shouldReturnEmptyListIfCartNotFoundUsingCustomerId(){
        // Given & When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomerUid(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));

        // Then
        assertThat(shoppingCarts.size(), is(0));
    }

    @Test
    public void shouldDeactivateShoppingCart(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final long cartId = shoppingCartRepository.findByUUID(uuid).get().getId();
        assertThat(shoppingCartRepository.findByUUID(uuid).get().isActive(), is(true));

        // When
        shoppingCartRepository.deactivateShoppingCart(cartId);

        // Then
        assertThat(shoppingCartRepository.findByUUID(uuid).isPresent(), is(false));
    }

    @Test
    public void shouldDeleteShoppingCartByUUID(){
        // Given
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final Long cartId = shoppingCartRepository.findByUUID(uuid).get().getId();
        shoppingCartRepository.addDeliveryOption(cartId, DeliveryOption.builder().build());
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
        shoppingCartRepository.addAddress(cartId, address);
        assertThat(shoppingCartRepository.findByUUID(uuid).isPresent(), is(true));

        // When
        shoppingCartRepository.delete(cartId);

        // Then
        assertThat(shoppingCartRepository.findByUUID(uuid).isPresent(), is(false));
    }

    @Test
    public void shouldAddAddress(){
        // Given
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
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
        address.setCartId(cart.getId());

        // When
        shoppingCartRepository.addAddress(cart.getId(), address);

        // Then
        Optional<Address> addressOptional = shoppingCartRepository.findAddress(cart.getId(), "Shipping");
        assertThat(addressOptional.get(), Matchers.equalTo(address));

        addressOptional = shoppingCartRepository.findAddress(cart.getId(), "Billing");
        assertThat(addressOptional.isPresent(), Matchers.is(false));

        // Given - Update address
        address.setTitle("Mrs.");
        address.setPostcode("SH13TT");
        address.setAddressLine1("11 Kings Road");

        // When
        shoppingCartRepository.addAddress(cart.getId(), address);

        // Then
        addressOptional = shoppingCartRepository.findAddress(cart.getId(), "Shipping");
        assertThat(addressOptional.get().getTitle(), Matchers.is("Mrs."));
        assertThat(addressOptional.get().getPostcode(), Matchers.is("SH13TT"));
        assertThat(addressOptional.get().getAddressLine1(), Matchers.is("11 Kings Road"));
    }

    @Test
    public void shouldAddDeliveryOption(){
        // Given
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final DeliveryOption deliveryOption = DeliveryOption.builder()
                .method("FREE Delivery")
                .charge(1.1d)
                .minDaysRequired(3)
                .maxDaysRequired(5)
                .cartId(cart.getId())
                .vatRate(20)
                .build();

        // When
        shoppingCartRepository.addDeliveryOption(cart.getId(), deliveryOption);

        // Then
        DeliveryOption actual = shoppingCartRepository.findDeliveryOption(cart.getId()).get();
        assertThat(actual.getMethod(), Matchers.is("FREE Delivery"));
        assertThat(actual.getCharge(), Matchers.is(1.1d));
        assertThat(actual.getMinDaysRequired(), Matchers.is(3));
        assertThat(actual.getMaxDaysRequired(), Matchers.is(5));
        assertThat(actual.getCreationTime(), Matchers.is(notNullValue()));
        assertThat(actual.getLastUpdateTime(), Matchers.is(nullValue()));
        assertThat(actual.getVatRate(), Matchers.is(20));

        // Given - Update delivery option
        deliveryOption.setMethod("Express Delivery");
        deliveryOption.setCharge(2.9d);
        deliveryOption.setMinDaysRequired(1);
        deliveryOption.setMaxDaysRequired(2);
        deliveryOption.setVatRate(10);

        // When
        shoppingCartRepository.addDeliveryOption(cart.getId(), deliveryOption);

        // Then
        actual = shoppingCartRepository.findDeliveryOption(cart.getId()).get();
        assertThat(actual.getMethod(), Matchers.is("Express Delivery"));
        assertThat(actual.getCharge(), Matchers.is(2.9d));
        assertThat(actual.getMinDaysRequired(), Matchers.is(1));
        assertThat(actual.getMaxDaysRequired(), Matchers.is(2));
        assertThat(actual.getCreationTime(), Matchers.is(notNullValue()));
        assertThat(actual.getLastUpdateTime(), Matchers.is(notNullValue()));
        assertThat(actual.getVatRate(), Matchers.is(10));
    }

    @Test
    public void shouldAddPromotion(){
        // Given
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("ABC-15")
                .vatRate(20)
                .discountAmount(BigDecimal.ONE)
                .cartId(cart.getId())
                .build();

        // When
        shoppingCartRepository.addPromotion(cart.getId(), promotion);

        // Then
        Promotion actual = shoppingCartRepository.findPromotion(cart.getId()).get();
        assertThat(actual.getVoucherCode(), Matchers.is("ABC-15"));
        assertThat(actual.getDiscountAmount(), Matchers.is(BigDecimal.ONE.setScale(2)));
        assertThat(actual.getCreationTime(), Matchers.is(notNullValue()));
        assertThat(actual.getLastUpdateTime(), Matchers.is(nullValue()));
        assertThat(actual.getVatRate(), Matchers.is(20));

        // Given
        promotion.setVoucherCode("ABC-16");
        promotion.setDiscountAmount(BigDecimal.TEN);
        promotion.setVatRate(0);

        // When
        shoppingCartRepository.addPromotion(cart.getId(), promotion);

        // Then
        actual = shoppingCartRepository.findPromotion(cart.getId()).get();
        assertThat(actual.getVoucherCode(), Matchers.is("ABC-16"));
        assertThat(actual.getDiscountAmount(), Matchers.is(BigDecimal.TEN.setScale(2)));
        assertThat(actual.getCreationTime(), Matchers.is(notNullValue()));
        assertThat(actual.getLastUpdateTime(), Matchers.is(notNullValue()));
        assertThat(actual.getVatRate(), Matchers.is(0));
    }

    @Test
    public void shouldDeletePromotion(){
        // Given
        final UUID uuid = shoppingCartRepository.create("123e4567-e89b-12d3-a456-556642440000", null);
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final Promotion promotion = Promotion.builder()
                .voucherCode("ABC-15")
                .vatRate(20)
                .discountAmount(BigDecimal.ONE)
                .cartId(cart.getId())
                .build();
        shoppingCartRepository.addPromotion(cart.getId(), promotion);
        Optional<Promotion> actual = shoppingCartRepository.findPromotion(cart.getId());
        assertThat(actual.isPresent(), is(true));

        // When
        shoppingCartRepository.deletePromotion(cart.getId());

        // Then
        actual = shoppingCartRepository.findPromotion(cart.getId());
        assertThat(actual.isPresent(), is(false));
    }
}