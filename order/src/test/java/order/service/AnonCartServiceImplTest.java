package order.service;

import order.data.AnonCartItemData;
import order.data.AnonCartItemDataBuilder;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.is;
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
        final AnonCartItemData anonCartItemData = new AnonCartItemData(null, 1, "book", 1.3, 10, 0, "url");
        when(anonCartRepository.findByCartUid(anonCartItemData.getCartUid())).thenReturn(Optional.empty());

        // When
        anonCartService.addItem(anonCartItemData);

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
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));
        final AnonCartItemData anotherCartItemData = new AnonCartItemData(anonCart.getCartUid(), 2, "pen", 0.5, 20, 0, "url2");

        // When
        final AnonCart actualCart = anonCartService.addItem(anotherCartItemData);

        // Then
        assertThat(actualCart.getTotalPrice(), is(23d));
        assertThat(actualCart.getTotalQuantity(), is(30));
        assertThat(actualCart.getAnonCartItems().size(), is(2));
    }

    @Test
    public void shouldUpdateQuantityIfItemIsAlreadyInTheCart(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        anonCart.addAnonCartItem(firstCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));
        final AnonCartItemData anotherCartItemData = new AnonCartItemData(anonCart.getCartUid(), 1, "book", 1.3, 11, 0, "url");

        // When
        final AnonCart actualCart = anonCartService.addItem(anotherCartItemData);

        // Then
        assertThat(actualCart.getTotalPrice(), is(27.3d));
        assertThat(actualCart.getTotalQuantity(), is(21));
        assertThat(actualCart.getAnonCartItems().size(), is(1));
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
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));

        // When
        final Optional<AnonCart> actualCart = anonCartService.updateCartWithCustomerId(anonCart.getCartUid(), customerId);

        // Then
        assertThat(actualCart.isPresent(), is(true));
        assertThat(actualCart.get().getCustomerId(), is(10293l));
    }

    @Test
    public void shouldReturnNullIfCartIsNotFound(){
        // Given
        final long customerId = 10293l;
        final AnonCart anonCart = new AnonCart();
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.empty());

        // When
        Optional<AnonCart> actualCart = anonCartService.updateCartWithCustomerId(anonCart.getCartUid(), customerId);

        // Then
        assertThat(actualCart.isPresent(), is(false));
    }

    @Test
    public void shouldDeleteCartItemByCartUidAndProductId(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        final AnonCartItem secondCartItem = new AnonCartItem(2, "pen", 1.2, 20, "url");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.addAnonCartItem(secondCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));

        // When
        anonCartService.deleteCartItemByProductId(anonCart.getCartUid(), 2l);

        // Then
        assertThat(anonCart.getAnonCartItems().size(), is(1));
        final AnonCartItem cartItem = anonCart.getAnonCartItems().iterator().next();
        assertThat(cartItem, is(firstCartItem));
    }

    @Test
    public void shouldDoNothingIfItemIsNotFoundByProductInTheCartWhenDeleting(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        final AnonCartItem secondCartItem = new AnonCartItem(2, "pen", 1.2, 20, "url");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.addAnonCartItem(secondCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));

        // When
        anonCartService.deleteCartItemByProductId(anonCart.getCartUid(), 3l);

        // Then
        assertThat(anonCart.getAnonCartItems().size(), is(2));
        assertThat(anonCart.getTotalPrice(), is(37D));
    }

    @Test
    public void shouldUpdateProductQuantity(){
        // Given
        final AnonCartItemData anonCartItemData = AnonCartItemDataBuilder.newBuilder().setQuantity(30).build();
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        final AnonCartItem secondCartItem = new AnonCartItem(2, "pen", 1.2, 20, "url");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.addAnonCartItem(secondCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));

        // When
        final Optional<AnonCart> actualCart = anonCartService.updateCartItemWithProductId(anonCart.getCartUid(), 2l, anonCartItemData);

        // Then
        assertThat(actualCart.isPresent(), is(true));
        assertThat(actualCart.get().getTotalQuantity(), is(40));
    }

    @Test
    public void shouldDoNothingIfItemIsNotFoundByProductInTheCartWhenUpdating(){
        // Given
        final AnonCartItemData anonCartItemData = AnonCartItemDataBuilder.newBuilder().setQuantity(30).build();
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(1, "book", 1.3, 10, "url");
        final AnonCartItem secondCartItem = new AnonCartItem(2, "pen", 1.2, 20, "url");
        anonCart.addAnonCartItem(firstCartItem);
        anonCart.addAnonCartItem(secondCartItem);
        when(anonCartRepository.findByCartUid(anonCart.getCartUid())).thenReturn(Optional.of(anonCart));

        // When
        Optional<AnonCart> actualCart = anonCartService.updateCartItemWithProductId(anonCart.getCartUid(), 3L, anonCartItemData);

        // Then
        assertThat(actualCart.isPresent(), is(true));
        assertThat(actualCart.get().getAnonCartItems().size(), is(2));
        assertThat(actualCart.get().getTotalQuantity(), is(30));
    }
}
