package product.mapper;

import org.junit.Test;
import product.data.ProductSimpleData;
import product.domain.Image;
import product.domain.ImageType;
import product.domain.Product;

import static org.hamcrest.CoreMatchers.is;
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

        // When
        final ProductSimpleData actual = mapper.getValue(product);

        // Then
        final ProductSimpleData expected = ProductSimpleData.builder().name("Chester").description("delicious").code("CH").imageUrl("url one").build();
        assertThat(actual, is(expected));

    }
}