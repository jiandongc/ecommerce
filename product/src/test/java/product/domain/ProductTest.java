package product.domain;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductTest {

    @Test
    public void shouldGetPricesWhenAllSkuAreOnSale() {
        // Given
        Product product = Product.builder().build();

        Sku sku = Sku.builder().build();
        sku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        sku.addPrice(Price.builder().startDate(now()).endDate(now().plusDays(1)).price(BigDecimal.valueOf(9)).discountRate("10%").build());
        product.addSku(sku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(9)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getDiscountRate(), is("10%"));
        assertThat(product.isOnSale(), is(true));

        // Given
        Sku anotherSku = Sku.builder().build();
        anotherSku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        anotherSku.addPrice(Price.builder().startDate(now()).endDate(now().plusDays(1)).price(BigDecimal.valueOf(8)).discountRate("20%").build());
        product.addSku(anotherSku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(8)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getDiscountRate(), is("20%"));
        assertThat(product.isOnSale(), is(true));
    }

    @Test
    public void shouldGetPricesWhenNoneSkuAreOnSale() {
        // Given
        Product product = Product.builder().build();

        Sku sku = Sku.builder().build();
        sku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        product.addSku(sku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getDiscountRate(), is(nullValue()));
        assertThat(product.isOnSale(), is(false));

        // Given
        Sku anotherSku = Sku.builder().build();
        anotherSku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        product.addSku(anotherSku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10)));
        assertThat(product.getDiscountRate(), is(nullValue()));
        assertThat(product.isOnSale(), is(false));
    }

    @Test
    public void shouldGetPricesWithTwoSkuOfWhichOnlyOneIsOnSale(){
        // Given
        final Product product = new Product();

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).discountRate("20%").price(BigDecimal.valueOf(0.8)).build());
        product.addSku(sku2);

        // When & Then
        Assert.assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(0.8)));
        Assert.assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(1)));
        Assert.assertThat(product.getDiscountRate(), is("20%"));
        Assert.assertThat(product.isOnSale(), is(true));
    }

    @Test
    public void shouldGetPriceWithTwoSkusOfWhichOnlyOneIsOnSaleBueExpired(){
        // Given
        final Product product = new Product();

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku2.addPrice(Price.builder().startDate(LocalDate.now().minusDays(10)).endDate(LocalDate.now().minusDays(9)).discountRate("20%").price(BigDecimal.valueOf(0.8)).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        product.addSku(sku2);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(1)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(1)));
        assertThat(product.getDiscountRate(), is(nullValue()));
        assertThat(product.isOnSale(), is(false));
    }

}