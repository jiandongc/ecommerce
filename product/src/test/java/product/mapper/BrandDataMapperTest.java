package product.mapper;

import org.junit.Test;
import product.data.BrandData;
import product.domain.Brand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class BrandDataMapperTest {

    private BrandDataMapper mapper = new BrandDataMapper();

    @Test
    public void shouldMapBrandToBrandData(){
//        // Given
//        final Brand brand = new Brand(1, "Walkers");
//
//        // When
//        final BrandData actual = mapper.getValue(brand);
//
//        // Then
//        final BrandData expected = new BrandData(1, "Walkers");
//        assertThat(actual, is(expected));
    }

}