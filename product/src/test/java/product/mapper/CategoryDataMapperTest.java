package product.mapper;

import org.junit.Test;
import product.data.CategoryData;
import product.domain.Category;
import product.domain.Product;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryDataMapperTest {

    private CategoryDataMapper mapper = new CategoryDataMapper();

    @Test
    public void shouldMapToCategoryData(){
        // Given
        final Category c1 = new Category();
        c1.setCode("c1");
        c1.setName("c1-n");

        final Category p1 = new Category();
        p1.setCode("p1");
        p1.setName("p1-n");
        final Category p2 = new Category();
        p2.setCode("p2");
        p2.setName("p2-n");
        final List<Category> parentsCategories = Arrays.asList(p1, p2);

        final Category s1 = new Category();
        s1.setCode("s1");
        s1.setName("s1-n");
        final Category s2 = new Category();
        s2.setCode("s2");
        s2.setName("s2-n");
        final Map<Category, Integer> subCategories = new LinkedHashMap<>();
        subCategories.put(s1, 1);
        subCategories.put(s2, 2);

        final Product pr1 = new Product();
        final Product pr2 = new Product();
        final Product pr3 = new Product();
        final List<Product> products = Arrays.asList(pr1, pr2, pr3);

        // When
        final CategoryData actual = mapper.getValue(c1, parentsCategories, subCategories, products);

        // Then
        final CategoryData p1d = CategoryData.builder()
                .code("p1")
                .name("p1-n")
                .build();
        final CategoryData p2d = CategoryData.builder()
                .code("p2")
                .name("p2-n")
                .build();
        final CategoryData s1d = CategoryData.builder()
                .code("s1")
                .name("s1-n")
                .productTotal(1)
                .build();
        final CategoryData s2d = CategoryData.builder()
                .code("s2")
                .name("s2-n")
                .productTotal(2)
                .build();

        final CategoryData expected = CategoryData.builder()
                .code("c1")
                .name("c1-n")
                .productTotal(3)
                .parents(Arrays.asList(p1d, p2d))
                .children(Arrays.asList(s1d, s2d))
                .build();

        assertThat(actual, is(expected));
    }
}