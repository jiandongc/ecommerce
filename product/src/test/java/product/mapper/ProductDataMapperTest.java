package product.mapper;

import org.junit.Test;
import product.data.ProductData;
import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductDataMapperTest {

    private ProductDataMapper mapper = new ProductDataMapper();

    @Test
    public void shouldMapProductToProductData(){
        // Given
        final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
        final Brand brand = new Brand(1, "Walkers");
        final Product product = new Product("Chester", 10d, "delicious", category, brand, "img/0001.jpg");

        // When
        final ProductData actual = mapper.getValue(product);

        // Then
        final ProductData expected = new ProductData(product.getId(), "Chester", 10d, "delicious", "food", "Walkers", "img/0001.jpg");
        assertThat(actual, is(expected));

    }

}