package shoppingcart.domain;

import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

public class ShoppingCartTest {

    @Test
    public void shouldCalculateItemsSubTotal(){
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(1.6d)
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(11.9d)
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal itemSubTotal = shoppingCart.getItemSubTotal();

        // Then
        assertThat(itemSubTotal, is(BigDecimal.valueOf(28.6)));
    }

    @Test
    public void shouldCalculateItemsVat(){
        // Given
        ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCart spy = Mockito.spy(shoppingCart);

        doReturn(BigDecimal.valueOf(28.6)).when(spy).getItemSubTotal();
        assertThat(spy.getItemsVat(), is(BigDecimal.valueOf(4.77)));

        doReturn(BigDecimal.valueOf(62.0)).when(spy).getItemSubTotal();
        assertThat(spy.getItemsVat(), is(BigDecimal.valueOf(10.33)));
    }

}