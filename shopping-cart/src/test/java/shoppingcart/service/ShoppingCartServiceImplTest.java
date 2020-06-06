package shoppingcart.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import shoppingcart.data.CustomerData;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository cartRepository;

    @Mock
    private ShoppingCartItemRepository cartItemRepository;

    @InjectMocks
    private ShoppingCartServiceImpl service = spy(new ShoppingCartServiceImpl());

    @Test
    public void shouldCreateShoppingCartForGuest(){
        // Given & When
        service.createShoppingCartForGuest();

        // Then
        verify(cartRepository, times(1)).create();
    }

    @Test
    public void shouldCreateShoppingCartForUser(){
        // Given & When
        service.createShoppingCartForUser(CustomerData.builder().id("123e4567-e89b-12d3-a456-556642440000").email("email").build());

        // Then
        verify(cartRepository, times(1)).create("123e4567-e89b-12d3-a456-556642440000", "email");
    }

    @Test
    public void shouldGetShoppingCartByUid(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));
        final Address shippingAddress = new Address();
        shippingAddress.setPostcode("SE6 7DE");
        when(cartRepository.findAddress(1L, "Shipping")).thenReturn(Optional.of(shippingAddress));
        final Address billingAddress = new Address();
        billingAddress.setPostcode("SE7 7DE");
        when(cartRepository.findAddress(1L, "Billing")).thenReturn(Optional.of(billingAddress));
        final DeliveryOption deliveryOption = DeliveryOption.builder().method("Free Delivery").build();
        when(cartRepository.findDeliveryOption(1L)).thenReturn(Optional.of(deliveryOption));

        // When
        final Optional<ShoppingCart> shoppingCart = service.getShoppingCartByUid(cartUid);

        // Then
        assertThat(shoppingCart.isPresent(), is(true));
        assertThat(shoppingCart.get().getId(), is(1L));
        assertThat(shoppingCart.get().getCartUid(), is(cartUid));
        assertThat(shoppingCart.get().getShoppingCartItems().size(), is(1));
        assertThat(shoppingCart.get().getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(shoppingCart.get().getShoppingCartItems().get(0).getSku(), is("123X7"));
        assertThat(shoppingCart.get().getShippingAddress().getPostcode(), is("SE6 7DE"));
        assertThat(shoppingCart.get().getBillingAddress().getPostcode(), is("SE7 7DE"));
        assertThat(shoppingCart.get().getDeliveryOption().getMethod(), is("Free Delivery"));
    }

    @Test
    public void shouldAddCustomerInfo(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final String customerId = "123e4567-e89b-12d3-a456-556642440000";
        final ShoppingCart shoppingCartOne = ShoppingCart.builder().cartUid(UUID.randomUUID()).build();
        final ShoppingCart shoppingCartTwo = ShoppingCart.builder().cartUid(UUID.randomUUID()).build();
        final ShoppingCart shoppingCartThree = ShoppingCart.builder().cartUid(cartUid).build();
        when(cartRepository.findByCustomerUid(UUID.fromString(customerId))).thenReturn(Arrays.asList(shoppingCartOne, shoppingCartTwo, shoppingCartThree));

        // When
        service.addCustomerInfo(cartUid, CustomerData.builder().id("123e4567-e89b-12d3-a456-556642440000").email("joe@gmail.com").build());

        // Then
        verify(service, times(1)).deactivateShoppingCart(shoppingCartOne);
        verify(service, times(1)).deactivateShoppingCart(shoppingCartTwo);
        verify(cartRepository, times(1)).updateCustomerUid(cartUid, UUID.fromString("123e4567-e89b-12d3-a456-556642440000"));
        verify(cartRepository, times(1)).updateEmail(cartUid,"joe@gmail.com");
    }

    @Test
    public void shouldDeactivateShoppingCart(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final ShoppingCart shoppingCart = ShoppingCart.builder().id(123L).cartUid(cartUid).build();

        // When
        service.deactivateShoppingCart(shoppingCart);

        // Then
        verify(cartRepository, times(1)).deactivateShoppingCart(123L);
    }

    @Test
    public void shouldGetShoppingCartByCustomerUid(){
        // Given
        final String customerUid = "123e4567-e89b-12d3-a456-556642440000";
        when(cartRepository.findByCustomerUid(UUID.fromString(customerUid))).thenReturn(Arrays.asList(ShoppingCart.builder().id(1L).customerUid(UUID.fromString(customerUid)).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));
        final Address shippingAddress = new Address();
        shippingAddress.setPostcode("SE6 7DE");
        when(cartRepository.findAddress(1L, "Shipping")).thenReturn(Optional.of(shippingAddress));
        final Address billingAddress = new Address();
        billingAddress.setPostcode("SE7 7DE");
        when(cartRepository.findAddress(1L, "Billing")).thenReturn(Optional.of(billingAddress));

        // When
        final Optional<ShoppingCart> shoppingCart = service.getShoppingCartByCustomerUid(customerUid);

        // Then
        assertThat(shoppingCart.isPresent(), is(true));
        assertThat(shoppingCart.get().getCustomerUid(), is(UUID.fromString("123e4567-e89b-12d3-a456-556642440000")));
        assertThat(shoppingCart.get().getId(), is(1L));
        assertThat(shoppingCart.get().getShoppingCartItems().size(), is(1));
        assertThat(shoppingCart.get().getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(shoppingCart.get().getShoppingCartItems().get(0).getSku(), is("123X7"));
        assertThat(shoppingCart.get().getShippingAddress().getPostcode(), is("SE6 7DE"));
        assertThat(shoppingCart.get().getBillingAddress().getPostcode(), is("SE7 7DE"));
    }

    @Test
    public void shouldGetEmptyIfNoShoppingCardIfFoundUsingCustomerUid(){
        // Given
        final String customerUid = "123e4567-e89b-12d3-a456-556642440000";
        when(cartRepository.findByCustomerUid(UUID.fromString(customerUid))).thenReturn(Collections.emptyList());

        // When
        final Optional<ShoppingCart> shoppingCart = service.getShoppingCartByCustomerUid(customerUid);

        // Then
        assertThat(shoppingCart.isPresent(), is(false));
    }

    @Test
    public void shouldAddAddress(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final Address address = new Address();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));

        // When
        service.addAddress(cartUid, address);

        // Then
        verify(cartRepository).addAddress(1L, address);
    }

    @Test
    public void shouldAddDeliveryOption(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final DeliveryOption deliveryOption = DeliveryOption.builder().build();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));

        // When
        service.addDeliveryOption(cartUid, deliveryOption);

        // Then
        verify(cartRepository).addDeliveryOption(1L, deliveryOption);
    }
}