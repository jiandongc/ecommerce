package shoppingcart.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ShoppingCartItemTest {

    @Test
    public void shouldCalculateItemVat_StandardVatRate(){
        // Given
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .price(BigDecimal.valueOf(1.99))
                .quantity(3)
                .vatRate(20)
                .build();

        // When
        BigDecimal vat = cartItem.getVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(0.99).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void shouldCalculateItemSale_StandardVatRate(){
        // Given
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .price(BigDecimal.valueOf(1.99))
                .quantity(3)
                .vatRate(20)
                .build();

        // When
        BigDecimal sale = cartItem.getNetAmount();

        // Then
        assertThat(sale, is(BigDecimal.valueOf(4.98).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void shouldCalculateItemVat_NoVat(){
        // Given
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .price(BigDecimal.valueOf(1.99))
                .quantity(3)
                .vatRate(0)
                .build();

        // When
        BigDecimal vat = cartItem.getVat();

        // Then
        assertThat(vat, is(BigDecimal.valueOf(0.00).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void shouldCalculateItemSale_NoVat(){
        // Given
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .price(BigDecimal.valueOf(1.99))
                .quantity(3)
                .vatRate(0)
                .build();

        // When
        BigDecimal sale = cartItem.getNetAmount();

        // Then
        assertThat(sale, is(BigDecimal.valueOf(5.97).setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

}