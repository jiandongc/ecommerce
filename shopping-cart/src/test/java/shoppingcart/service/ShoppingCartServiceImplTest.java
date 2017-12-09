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
import static org.mockito.Mockito.when;

public class ShoppingCartServiceImplTest {

    private ShoppingCartRepository cartRepository = mock(ShoppingCartRepository.class);
    private ShoppingCartItemRepository cartItemRepository = mock(ShoppingCartItemRepository.class);
    private ShoppingCartService service = new ShoppingCartServiceImpl(cartRepository, cartItemRepository);

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
        assertThat(shoppingCart.getId(), is(1L));
        assertThat(shoppingCart.getCartUid(), is(cartUid));
        assertThat(shoppingCart.getShoppingCartItems().size(), is(1));
        assertThat(shoppingCart.getShoppingCartItems().get(0).getName(), is("product"));
        assertThat(shoppingCart.getShoppingCartItems().get(0).getSku(), is("123X7"));
    }

}