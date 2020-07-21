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
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal itemSubTotal = shoppingCart.getItemSubTotal();

        // Then
        assertThat(itemSubTotal, is(BigDecimal.valueOf(28.6).setScale(2)));
    }

    @Test
    public void shouldCalculateItemsVat(){
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal vat = shoppingCart.getItemsVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(4.77).setScale(2)));
    }

    @Test
    public void shouldCalculateItemsVatWithMixedRate(){
        // Given
        final ShoppingCart shoppingCart = ShoppingCart.builder().build();
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .code("code1")
                .price(BigDecimal.valueOf(1.6))
                .quantity(3)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .vatRate(20)
                .build();
        shoppingCart.addItem(cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(11.9))
                .quantity(2)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .vatRate(0)
                .build();
        shoppingCart.addItem(cartItem2);

        // When
        BigDecimal vat = shoppingCart.getItemsVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(0.80).setScale(2)));
    }


}