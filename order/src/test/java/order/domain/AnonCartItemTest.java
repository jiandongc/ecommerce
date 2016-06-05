package order.domain;


import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 09/04/16.
 */
public class AnonCartItemTest {

    @Test
    public void shouldCalculateSubTotalForIndividualCartItem(){
        // Given
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 2, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 2, 10, "http://pen.jpeg");

        // When
        double itemOneSubTotal = itemOne.getSubTotal();
        double itemTwoSubTotal = itemTwo.getSubTotal();

        // Then
        assertThat(itemOneSubTotal, is(24d));
        assertThat(itemTwoSubTotal, is(20d));
    }
}