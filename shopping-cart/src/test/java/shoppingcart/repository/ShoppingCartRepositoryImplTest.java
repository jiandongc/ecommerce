package shoppingcart.repository;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shoppingcart.domain.Address;
import shoppingcart.domain.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        final UUID uuid = shoppingCartRepository.create(1234L);

        // Then
        final ShoppingCart shoppingCart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerId(), is(1234L));
    }

    @Test
    public void shouldReturnEmptyOptionalIfCartUidIsNotFound(){
        // Given & When
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUUID(UUID.randomUUID());

        // Then
        assertThat(cart.isPresent(), is(false));
    }

    @Test
    public void shouldUpdateShoppingCartCustomerId(){
        // Given
        final UUID uuid = shoppingCartRepository.create();

        // When
        int rowUpdated = shoppingCartRepository.updateCustomerId(uuid, 123L);

        // Then
        assertThat(rowUpdated, is(1));
        assertThat(shoppingCartRepository.findByUUID(uuid).get().getCustomerId(), is(123L));
    }

    @Test
    public void shouldReturnShoppingCartByCustomerId(){
        // Given
        shoppingCartRepository.create(1234L);
        shoppingCartRepository.create(1234L);

        // When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomerId(1234L);

        // Then
        assertThat(shoppingCarts.size(), is(2));
    }

    @Test
    public void shouldReturnEmptyListIfCartNotFoundUsingCustomerId(){
        // Given & When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByCustomerId(9999L);

        // Then
        assertThat(shoppingCarts.size(), is(0));
    }

    @Test
    public void shouldDeleteShoppingCartByUUID(){
        // Given
        final UUID uuid = shoppingCartRepository.create(1234L);
        assertThat(shoppingCartRepository.findByUUID(uuid).isPresent(), is(true));

        // When
        shoppingCartRepository.delete(uuid);

        // Then
        assertThat(shoppingCartRepository.findByUUID(uuid).isPresent(), is(false));
    }

    @Test
    public void shouldAddAddress(){
        // Given
        final UUID uuid = shoppingCartRepository.create(1234L);
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
}