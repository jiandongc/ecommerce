package order.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.util.collections.Sets.newSet;

public class AnonCartTest {

    @Test
    public void shouldGetTotalCount() {
        // Given
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        final AnonCart anonCart = new AnonCart(newSet(itemOne, itemTwo));

        // When
        int totalCount = anonCart.getTotalCount();

        // Then
        assertThat(totalCount, is(2));
    }

    @Test
    public void shouldGetTotalPrice() {
        // Given
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        final AnonCart anonCart = new AnonCart(newSet(itemOne, itemTwo));

        // When
        double totalPrice = anonCart.getTotalPrice();

        // Then
        assertThat(totalPrice, is(22D));
    }
}