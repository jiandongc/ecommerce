package product.mapper;

import org.junit.Test;
import product.data.ProductSimpleData;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by jiandong on 03/09/17.
 */
public class ProductSimpleDataMapperTest {

    private ProductSimpleDataMapper mapper = new ProductSimpleDataMapper();

    @Test
    public void shouldMapProductToProductSimpleDataWithMainImage() {
        // Given
        final Product product = new Product();
        product.setName("Chester");
        product.setDescription("delicious");
        product.setCode("CH");

        final Image imageOne = new Image();
        final ImageType imageTypeOne = new ImageType();
        imageTypeOne.setType("main");
        imageOne.setUrl("url one");
        imageOne.setImageType(imageTypeOne);

        final Image imageTwo = new Image();
        final ImageType imageTypeTwo = new ImageType();
        imageTypeTwo.setType("thumbnail");
        imageTwo.setUrl("url two");
        imageTwo.setImageType(imageTypeTwo);

        product.addImage(imageTwo);
        product.addImage(imageOne);

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(0.9)).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        product.addSku(sku2);

        // When
        final ProductSimpleData actual = mapper.getValueWithMainImage(product);

        // Then
        final ProductSimpleData expected = ProductSimpleData.builder()
                .name("Chester")
                .code("CH")
                .imageUrl("url one")
                .price(BigDecimal.valueOf(0.9))
                .originalPrice(BigDecimal.valueOf(0.9))
                .discountRate(null)
                .isOnSale(false)
                .build();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldMapProductToProductSimpleDataWithColorImage() {
        // Given
        final Product product = new Product();
        product.setName("Chester");
        product.setDescription("delicious");
        product.setCode("CH");

        final Image imageOne = new Image();
        final ImageType imageTypeOne = new ImageType();
        imageTypeOne.setType("main");
        imageOne.setUrl("url one");
        imageOne.setImageType(imageTypeOne);

        final Image imageTwo = new Image();
        final ImageType imageTypeTwo = new ImageType();
        imageTypeTwo.setType("color");
        imageTwo.setUrl("url two");
        imageTwo.setImageType(imageTypeTwo);

        product.addImage(imageTwo);
        product.addImage(imageOne);

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku1.addPrice(Price.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).discountRate("10%").price(BigDecimal.valueOf(0.9)).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(1)).build());
        sku2.addPrice(Price.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).discountRate("20%").price(BigDecimal.valueOf(0.8)).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        product.addSku(sku2);

        // When
        final ProductSimpleData actual = mapper.getValueWithColorImage(product);

        // Then
        final ProductSimpleData expected = ProductSimpleData.builder()
                .name("Chester")
                .code("CH")
                .imageUrl("url two")
                .originalPrice(BigDecimal.valueOf(1))
                .price(BigDecimal.valueOf(0.8))
                .discountRate("20%")
                .isOnSale(true)
                .build();
        assertThat(actual, is(expected));
    }

    @Test
    public void shouldMapProductWithTwoSkuOfWhichOnlyOneIsOnSaleButExpired(){
        // Given
        final Product product = new Product();

        final Image imageOne = new Image();
        final ImageType imageTypeOne = new ImageType();
        imageTypeOne.setType("main");
        imageOne.setUrl("url one");
        imageOne.setImageType(imageTypeOne);
        product.addImage(imageOne);

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

        // When
        final ProductSimpleData actual = mapper.getValueWithMainImage(product);

        // Then
        assertThat(actual.getPrice(), is(BigDecimal.valueOf(1)));
        assertThat(actual.getOriginalPrice(), is(BigDecimal.valueOf(1)));
        assertThat(actual.getDiscountRate(), is(nullValue()));
        assertThat(actual.isOnSale(), is(false));
    }
}