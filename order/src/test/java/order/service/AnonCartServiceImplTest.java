package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.junit.Test;

import java.util.UUID;

import static java.util.UUID.randomUUID;
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
        final AnonCartItemData anonCartItemData = new AnonCartItemData(null, 1, "book", 1.3, 10);

        // When
        anonCartService.addFirstItem(anonCartItemData);

        // Then
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10);
        anonCart.addAnonCartItem(firstCartItem);

        verify(anonCartRepository).save(refEq(anonCart, "cartUid"));
    }

    @Test
    public void shouldBeAbleToAddCartItemIntoExistingCart(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10);
        anonCart.addAnonCartItem(firstCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(anonCart);
        final AnonCartItemData anotherCartItemData = new AnonCartItemData(anonCart.getCartUid(), 2, "pen", 0.5, 20);

        // When
        anonCartService.addAnotherItem(anotherCartItemData);

        // Then
        final AnonCartItem anotherCartItem = new AnonCartItem(2, "pen", 0.5, 20);
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
}
