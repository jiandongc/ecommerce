package product.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SkuTest {

    @Test
    public void shouldGetPrice(){
        // Given
        Price priceOne = Price.builder()
                .price(BigDecimal.ONE)
                .startDate(LocalDate.of(2018, 10, 10))
                .build();
        Price priceTwo = Price.builder()
                .price(BigDecimal.TEN)
                .startDate(LocalDate.of(2050, 10, 10))
                .build();
        Sku sku = new Sku();
        sku.addPrice(priceOne);
        sku.addPrice(priceTwo);

        // When && Then
        assertThat(sku.getOriginalPrice(), is(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(sku.getCurrentPrice(), is(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

    @Test
    public void shouldGetCurrentSalePrice(){
        // Given
        Price priceOne = Price.builder()
                .price(BigDecimal.ONE)
                .startDate(LocalDate.of(2018, 10, 10))
                .endDate(LocalDate.of(2018, 10, 11))
                .build();
        Price priceTwo = Price.builder()
                .price(BigDecimal.TEN)
                .startDate(LocalDate.of(2018, 10, 12))
                .endDate(LocalDate.of(2050, 10, 12))
                .build();
        Price priceThree = Price.builder()
                .price(BigDecimal.ZERO)
                .startDate(LocalDate.of(2050, 10, 13))
                .endDate(LocalDate.of(2050, 10, 14))
                .build();

        Sku sku = new Sku();
        sku.addPrice(priceOne);
        sku.addPrice(priceTwo);
        sku.addPrice(priceThree);

        // When && Then
        assertThat(sku.getCurrentSalePrice(), is(BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(sku.getCurrentPrice(), is(BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_UP)));
    }

}