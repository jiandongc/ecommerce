package shoppingcart.service;

import org.junit.Test;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
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
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));
        when(cartItemRepository.findByCartIdAndSku(1L, "123X7")).thenReturn(empty());

        // When
        final ShoppingCart cart = service.createCartItem(cartUid, cartItem);

        // Then
        verify(cartItemRepository, times(1)).save(1L, cartItem);
        assertThat(cart.getId(), is(1L));
        assertThat(cart.getCartUid(), is(cartUid));
        assertThat(cart.getShoppingCartItems().size(), is(1));
        assertThat(cart.getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(cart.getShoppingCartItems().get(0).getSku(), is("123X7"));
    }

    @Test
    public void shouldAddSameItemToACart(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartIdAndSku(1L, "123X7")).thenReturn(Optional.of(ShoppingCartItem.builder().quantity(2).build()));

        // When
        service.createCartItem(cartUid, cartItem);

        // Then
        verify(cartItemRepository, times(1)).updateQuantity(1L, "123X7", 3);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfCartUidIsNotFoundWhenAddItem(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(empty());
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").build();

        // When
        service.createCartItem(cartUid, cartItem);
    }

    @Test
    public void shouldDeleteItemFromACart(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder().cartId(1L).name("product").sku("123X7").build();
        when(cartItemRepository.findByCartId(1L)).thenReturn(asList(cartItem));

        // When
        ShoppingCart cart = service.deleteCartItem(cartUid, "12345");

        // Then
        verify(cartItemRepository).delete(1L, "12345");
        assertThat(cart.getId(), is(1L));
        assertThat(cart.getCartUid(), is(cartUid));
        assertThat(cart.getShoppingCartItems().size(), is(1));
        assertThat(cart.getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(cart.getShoppingCartItems().get(0).getSku(), is("123X7"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfCartUidIsNotFoundWhenDeleteItem(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(empty());

        // When
        service.deleteCartItem(cartUid, "12345");
    }

    @Test
    public void shouldUpdateCartItemQuantity(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.of(ShoppingCart.builder().id(1L).cartUid(cartUid).build()));

        // When
        service.updateQuantity(cartUid, "sku", 10);

        // Then
        verify(cartItemRepository).updateQuantity(1L, "sku", 10);
    }

    @Test
    public void shouldNotUpdateQuantityIfCartIsNotFound(){
        // Given
        final UUID cartUid = UUID.randomUUID();
        when(cartRepository.findByUUID(cartUid)).thenReturn(Optional.empty());

        // When
        service.updateQuantity(cartUid, "sku", 10);

        // Then
        verifyZeroInteractions(cartItemRepository);
    }
}