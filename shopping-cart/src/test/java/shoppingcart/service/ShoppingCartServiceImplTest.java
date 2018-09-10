package shoppingcart.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Arrays;
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
        service.createShoppingCartForUser(1234L);

        // Then
        verify(cartRepository, times(1)).create(1234L);
    }

    @Test
    public void shouldGetShoppingCartByUid(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));

        // When
        final ShoppingCart shoppingCart = service.getShoppingCartByUid(cartUid);

        // Then
        assertThat(shoppingCart.getId(), is(1L));
        assertThat(shoppingCart.getCartUid(), is(cartUid));
        assertThat(shoppingCart.getShoppingCartItems().size(), is(1));
        assertThat(shoppingCart.getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(shoppingCart.getShoppingCartItems().get(0).getSku(), is("123X7"));
    }

    @Test
    public void shouldUpdateCustomerId(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final Long customerId = 123L;
        final ShoppingCart shoppingCartOne = ShoppingCart.builder().cartUid(UUID.randomUUID()).build();
        final ShoppingCart shoppingCartTwo = ShoppingCart.builder().cartUid(UUID.randomUUID()).build();
        final ShoppingCart shoppingCartThree = ShoppingCart.builder().cartUid(cartUid).build();
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Arrays.asList(shoppingCartOne, shoppingCartTwo, shoppingCartThree));

        // When
        service.updateCustomerId(cartUid, 123L);

        // Then
        verify(service, times(1)).deleteShoppingCart(shoppingCartOne);
        verify(service, times(1)).deleteShoppingCart(shoppingCartTwo);
        verify(cartRepository, times(1)).updateCustomerId(cartUid,123L);
        verify(cartRepository, times(1)).findByUUID(cartUid);
    }

    @Test
    public void shouldDeleteShoppingCart(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        final ShoppingCart shoppingCart = ShoppingCart.builder().id(123L).cartUid(cartUid).build();

        // When
        service.deleteShoppingCart(shoppingCart);

        // Then
        verify(cartItemRepository, times(1)).deleteByCartId(123L);
        verify(cartRepository, times(1)).delete(cartUid);
    }
}