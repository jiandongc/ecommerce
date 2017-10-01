package product.mapper;

import org.junit.Test;
import product.data.ProductSimpleData;
import product.domain.*;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.CoreMatchers.is;
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
        sku1.setPrice(TEN);
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.setPrice(ONE);
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
                .price(ONE)
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
        sku1.setPrice(TEN);
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.setPrice(ONE);
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
                .price(ONE)
                .build();
        assertThat(actual, is(expected));

    }
}