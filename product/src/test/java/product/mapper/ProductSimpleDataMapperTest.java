package product.mapper;

import org.junit.Test;
import product.data.ProductSimpleData;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by jiandong on 03/09/17.
 */
public class ProductSimpleDataMapperTest {

    private ProductSimpleDataMapper mapper = new ProductSimpleDataMapper();

    @Test
    public void shouldMapProductToProductSimpleData() {
        // Given
        final Product product = new Product();
        product.setName("Chester");
        product.setDescription("delicious");
        product.setCode("CH");

        final Image imageOne = new Image();
        imageOne.setOrdering(1);
        imageOne.setUrl("url one");

        final Image imageTwo = new Image();
        imageTwo.setOrdering(2);
        imageTwo.setUrl("url two");

        product.addImage(imageOne);
        product.addImage(imageTwo);

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(0.9).setScale(2, ROUND_HALF_UP)).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        product.addSku(sku2);

        product.addTag(ProductTag.builder().tag("sale").code("sale").startDate(LocalDate.now()).colorHex("#F0C14B").build());

        // When
        final ProductSimpleData actual = mapper.map(product);

        // Then
        final List<Map<String, String>> tags = new ArrayList<>();
        final Map<String, String> saleTag = new HashMap<>();
        saleTag.put("code", "sale");
        saleTag.put("tag", "sale");
        saleTag.put("colorHex", "#F0C14B");
        tags.add(saleTag);

        final ProductSimpleData expected = ProductSimpleData.builder()
                .name("Chester")
                .code("CH")
                .imageUrl("url one")
                .price(BigDecimal.valueOf(0.9).setScale(2, ROUND_HALF_UP))
                .originalPrice(BigDecimal.valueOf(0.9).setScale(2, ROUND_HALF_UP))
                .discountRate(null)
                .isOnSale(false)
                .tags(tags)
                .build();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldMapProductWithTwoSkuOfWhichOnlyOneIsOnSaleButExpired(){
        // Given
        final Product product = new Product();

        final Image imageOne = new Image();
        imageOne.setUrl("url one");
        product.addImage(imageOne);

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)).build());
        sku2.addPrice(Price.builder().startDate(LocalDate.now().minusDays(10)).endDate(LocalDate.now().minusDays(9)).discountRate("20%").price(BigDecimal.valueOf(0.8).setScale(2, ROUND_HALF_UP)).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        product.addSku(sku2);

        // When
        final ProductSimpleData actual = mapper.map(product);

        // Then
        assertThat(actual.getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(actual.getOriginalPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(actual.getDiscountRate(), is(nullValue()));
        assertThat(actual.isOnSale(), is(false));
    }
}