package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.junit.Test;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnonCartServiceImplTest {

    private AnonCartRepository anonCartRepository = mock(AnonCartRepository.class);
    private AnonCartService anonCartService = new AnonCartServiceImpl(anonCartRepository);

    @Test
    public void shouldSaveNewAnonCartWhenAddTheFirstCartItem(){
        // Given
        final AnonCartItemData anonCartItemData = new AnonCartItemData(null, 1, "book", 1.3, 10, "url");

        // When
        anonCartService.addFirstItem(anonCartItemData);

        // Then
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        anonCart.addAnonCartItem(firstCartItem);

        verify(anonCartRepository).save(refEq(anonCart, "cartUid"));
    }

    @Test
    public void shouldBeAbleToAddCartItemIntoExistingCart(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        anonCart.addAnonCartItem(firstCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(anonCart);
        final AnonCartItemData anotherCartItemData = new AnonCartItemData(anonCart.getCartUid(), 2, "pen", 0.5, 20, "url2");

        // When
        anonCartService.addAnotherItem(anotherCartItemData);

        // Then
        final AnonCartItem anotherCartItem = new AnonCartItem(2, "pen", 0.5, 20, "url2");
        anonCart.addAnonCartItem(anotherCartItem);
        verify(anonCartRepository).save(anonCart);
    }

    @Test
    public void shouldFindAnonCartByCartUid(){
        // Given
        final UUID cartUid = randomUUID();

        // When
        anonCartService.findAnonCartByUid(cartUid);

        // Then
        verify(anonCartRepository).findByCartUid(cartUid);
    }

    @Test
    public void shouldFindAnonCartByCustomerId(){
        // Given
        final long customerId = 12345l;

        // When
        anonCartService.findAnonCartByCustomerId(customerId);

        // Then
        verify(anonCartRepository).findByCustomerId(customerId);
    }

    @Test
    public void shouldUpdateCustomerId(){
        // Given
        final long customerId = 10293l;
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        anonCart.addAnonCartItem(firstCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(anonCart);

        // When
        AnonCart actualCart = anonCartService.updateCustomerId(anonCart.getCartUid(), customerId);

        // Then
        anonCart.setCustomerId(customerId);
        assertThat(actualCart, is(anonCart));
    }

    @Test
    public void shouldReturnNullIfCartIsNotFound(){
        // Given
        final long customerId = 10293l;
        final AnonCart anonCart = new AnonCart();
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(null);

        // When
        AnonCart actualCart = anonCartService.updateCustomerId(anonCart.getCartUid(), customerId);

        // Then
        assertThat(actualCart, is(nullValue()));
    }
}
