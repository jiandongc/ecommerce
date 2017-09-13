package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import product.domain.Category;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 05/11/16.
 */
public class CategoryRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldFindCategoryByCode(){
        // Given
        Category category = new Category();
        category.setHidden(false);
        category.setName("food");
        category.setDescription("delicious");
        category.setImageUrl("img/0001.jpg");
        category.setCode("FD");
        categoryRepository.save(category);

        // When
        final Optional<Category> actualCategory = categoryRepository.findByCode("FD");

        // Then
        assertThat(actualCategory.get(), is(category));
    }

    @Test
    public void shouldFindSubCategoriesByCode(){
        // Given
        Category category = new Category();
        category.setHidden(false);
        category.setName("food");
        category.setDescription("delicious");
        category.setImageUrl("img/0001.jpg");
        category.setCode("FD");
        categoryRepository.save(category);

        Category subCategory1 = new Category();
        subCategory1.setName("crackers");
        subCategory1.setCode("CR");
        subCategory1.setParent(category);
        categoryRepository.save(subCategory1);

        Category subCategory2 = new Category();
        subCategory2.setName("cake");
        subCategory2.setCode("CA");
        subCategory2.setParent(category);
        categoryRepository.save(subCategory2);

        Category subCategory3 = new Category();
        subCategory3.setName("chocolate");
        subCategory3.setCode("CH");
        subCategory3.setParent(category);
        categoryRepository.save(subCategory3);

        // When
        final List<Category> subCategories = categoryRepository.findSubCategoriesByCode("FD");

        // Then
        assertThat(subCategories.size(), is(3));
        assertThat(subCategories, hasItems(subCategory1, subCategory2, subCategory3));
    }

}
