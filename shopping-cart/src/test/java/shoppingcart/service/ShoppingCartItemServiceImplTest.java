package shoppingcart.service;

import org.junit.Test;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class ShoppingCartItemServiceImplTest {

    private ShoppingCartRepository cartRepository = mock(ShoppingCartRepository.class);
    private ShoppingCartItemRepository cartItemRepository = mock(ShoppingCartItemRepository.class);
    private ShoppingCartItemService service = new ShoppingCartItemServiceImpl(cartRepository, cartItemRepository);

    @Test
    public void shouldAddItemToCart(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));

        // When
        final ShoppingCart cart = service.createCartItem(cartUid, cartItem);

        // Then
        verify(cartItemRepository, times(1)).save(1L, cartItem);
        assertThat(cart.getId(), is(1L));
        assertThat(cart.getCartUid(), is(cartUid));
        assertThat(cart.getShoppingCartItems().size(), is(1));
        assertThat(cart.getShoppingCartItems().get(0).getName(), is("product"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfCartUidIsNotFound(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.empty());
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").build();

        // When
        service.createCartItem(UUID.randomUUID(), cartItem);
    }
}