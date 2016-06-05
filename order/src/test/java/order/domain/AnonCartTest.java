package order.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnonCartTest {

    @Test
    public void shouldGetTotalCount() {
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10, "http://pen.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);

        // When
        int totalQuantity = anonCart.getTotalQuantity();

        // Then
        assertThat(totalQuantity, is(11));
    }

    @Test
    public void shouldGetTotalPrice() {
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10, "http://pen.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);

        // When
        double totalPrice = anonCart.getTotalPrice();

        // Then
        assertThat(totalPrice, is(22D));
    }
}