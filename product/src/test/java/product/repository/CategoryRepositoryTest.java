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
    public void shouldFindCategoryById(){
        // Given
        final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
        categoryRepository.save(category);

        // When
        final Optional<Category> actualCategory = categoryRepository.findById(1l);

        // Then
        assertThat(actualCategory.isPresent(), is(true));
        assertThat(actualCategory.get(), is(category));
    }

    @Test
    public void shouldFindSubCategoriesByParentId(){
        // Given
        final Category categoryOne = new Category(2, "food", "delicious", "img/0005.jpg", 1);
        final Category categoryTwo = new Category(3, "cloth", "delicious", "img/0006.jpg", 1);
        final Category categoryThree = new Category(4, "makeup", "delicious", "img/0007.jpg", 1);
        final Category categoryFour = new Category(5, "makeup", "delicious", "img/0007.jpg", 6);
        categoryRepository.save(categoryOne);
        categoryRepository.save(categoryTwo);
        categoryRepository.save(categoryThree);
        categoryRepository.save(categoryFour);

        // When
        final List<Category> subCategories = categoryRepository.findByParentId(1l);

        // Then
        assertThat(subCategories, hasItems(categoryOne, categoryTwo, categoryThree));
    }

}
