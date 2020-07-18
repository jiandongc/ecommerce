package product.domain;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(9).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getDiscountRate(), is("10%"));
        assertThat(product.isOnSale(), is(true));

        // Given
        Sku anotherSku = Sku.builder().build();
        anotherSku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        anotherSku.addPrice(Price.builder().startDate(now()).endDate(now().plusDays(1)).price(BigDecimal.valueOf(8)).discountRate("20%").build());
        product.addSku(anotherSku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(8).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
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
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getDiscountRate(), is(nullValue()));
        assertThat(product.isOnSale(), is(false));

        // Given
        Sku anotherSku = Sku.builder().build();
        anotherSku.addPrice(Price.builder().startDate(now()).price(BigDecimal.valueOf(10)).build());
        product.addSku(anotherSku);

        // When & Then
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(10).setScale(2, BigDecimal.ROUND_HALF_UP)));
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
        Assert.assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(0.8).setScale(2, BigDecimal.ROUND_HALF_UP)));
        Assert.assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
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
        assertThat(product.getCurrentPrice(), is(BigDecimal.valueOf(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getOriginalPrice(), is(BigDecimal.valueOf(1).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product.getDiscountRate(), is(nullValue()));
        assertThat(product.isOnSale(), is(false));
    }

    @Test
    public void shouldGetValidDisplayTags(){
        // Given
        final Product product = new Product();
        product.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now()).build());
        product.addTag(ProductTag.builder().tag("popular").startDate(LocalDate.now().minusDays(1)).build());
        product.addTag(ProductTag.builder().tag("future").startDate(LocalDate.now().plusDays(1)).build());
        product.addTag(ProductTag.builder().tag("past").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().minusDays(1)).build());

        // When
        List<ProductTag> validTags = product.getValidTags();

        // Then
        assertThat(validTags.size(), is(2));
        assertThat(validTags.get(0).getTag(), is("sale"));
        assertThat(validTags.get(1).getTag(), is("popular"));
    }

    @Test
    public void shouldReturnTrueIfProductHasTag(){
        // Given
        final Product product = new Product();
        product.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now()).build());
        product.addTag(ProductTag.builder().tag("popular").startDate(LocalDate.now().minusDays(1)).build());
        product.addTag(ProductTag.builder().tag("future").startDate(LocalDate.now().plusDays(1)).build());
        product.addTag(ProductTag.builder().tag("past").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().minusDays(1)).build());

        // Then
        assertThat(product.hasTag(Arrays.asList("sale")), is(true));
        assertThat(product.hasTag(Arrays.asList("popular")), is(true));
        assertThat(product.hasTag(Arrays.asList("SALE")), is(true));
        assertThat(product.hasTag(Arrays.asList("POPULAR")), is(true));
        assertThat(product.hasTag(Arrays.asList("sale", "unknown")), is(true));
        assertThat(product.hasTag(Arrays.asList("popular", "unknown")), is(true));
        assertThat(product.hasTag(Arrays.asList("SALE", "unknown")), is(true));
        assertThat(product.hasTag(Arrays.asList("POPULAR", "unknown")), is(true));
    }

    @Test
    public void shouldReturnFalseIfProductHasNoTag(){
        // Given
        final Product product = new Product();
        product.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now()).build());
        product.addTag(ProductTag.builder().tag("popular").startDate(LocalDate.now().minusDays(1)).build());
        product.addTag(ProductTag.builder().tag("future").startDate(LocalDate.now().plusDays(1)).build());
        product.addTag(ProductTag.builder().tag("past").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().minusDays(1)).build());

        assertThat(product.hasTag(Arrays.asList("future")), is(false));
        assertThat(product.hasTag(Arrays.asList("past")), is(false));
        assertThat(product.hasTag(Arrays.asList("FUTURE")), is(false));
        assertThat(product.hasTag(Arrays.asList("PAST")), is(false));
        assertThat(product.hasTag(Arrays.asList("future", "unknown")), is(false));
        assertThat(product.hasTag(Arrays.asList("past", "unknown")), is(false));
        assertThat(product.hasTag(Arrays.asList("FUTURE", "unknown")), is(false));
        assertThat(product.hasTag(Arrays.asList("PAST", "unknown")), is(false));
        assertThat(product.hasTag(Arrays.asList("unknown")), is(false));
        assertThat(product.hasTag(Arrays.asList("unknown", "unknown1")), is(false));
    }

    @Test
    public void shouldReturnTrueIfProductIsActive(){
        // Given
        Product product = new Product();
        product.setStartDate(LocalDate.now());

        assertThat(product.isActive(), is(true));

        product = new Product();
        product.setStartDate(LocalDate.now().minusDays(10));

        assertThat(product.isActive(), is(true));

        product = new Product();
        product.setStartDate(LocalDate.now());
        product.setEndDate(LocalDate.now().plusDays(10));

        assertThat(product.isActive(), is(true));

        product = new Product();
        product.setStartDate(LocalDate.now().minusDays(10));
        product.setEndDate(LocalDate.now().plusDays(10));

        assertThat(product.isActive(), is(true));

        product = new Product();
        product.setStartDate(LocalDate.now().minusDays(10));
        product.setEndDate(LocalDate.now());

        assertThat(product.isActive(), is(true));
    }

    @Test
    public void shouldReturnFalseIfProductIsInactive(){
        // Given
        Product product = new Product();
        product.setStartDate(LocalDate.now().plusDays(10));

        assertThat(product.isActive(), is(false));

        product = new Product();
        product.setStartDate(LocalDate.now().minusDays(10));
        product.setEndDate(LocalDate.now().minusDays(1));

        assertThat(product.isActive(), is(false));
    }

}