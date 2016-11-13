package product.mapper;

import org.junit.Test;
import product.data.CategoryData;
import product.domain.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryDataMapperTest {

    private CategoryDataMapper mapper = new CategoryDataMapper();

    @Test
    public void shouldMapCategoryToCategoryData(){
        // Given
        final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);

        // When
        final CategoryData actual = mapper.getValue(category);

        // Then
        final CategoryData expected = new CategoryData(1, "food");
        assertThat(actual, is(expected));
    }
}