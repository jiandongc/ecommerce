package order.mapper;

import order.data.AnonCartItemData;
import order.data.CartSummaryData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jiandong on 31/03/16.
 */
public class CartSummaryDataMapperTest {

    final CartSummaryDataMapper mapper = new CartSummaryDataMapper();

    @Test
    public void shouldMapAnonCartToCartSummaryData(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem cartItemOne = new AnonCartItem(1, "book", 1, 10);
        final AnonCartItem cartItemTwo = new AnonCartItem(2, "pen", 2, 11);
        anonCart.addAnonCartItem(cartItemOne);
        anonCart.addAnonCartItem(cartItemTwo);

        // When
        final CartSummaryData actual = mapper.getValue(anonCart);

        // Then
        final UUID cartUid = anonCart.getCartUid();
        final AnonCartItemData cartItemDataOne = new AnonCartItemData(cartUid, 1, "book", 1d, 10);
        final AnonCartItemData cartItemDataTwo = new AnonCartItemData(cartUid, 2, "pen", 2d, 11);
        final CartSummaryData expected = new CartSummaryData(cartUid, 2, 32, Sets.newSet(cartItemDataOne, cartItemDataTwo));
        assertThat(actual, is(expected));
    }
}