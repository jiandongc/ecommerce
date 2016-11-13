package product.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import product.domain.Category;
import product.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryServiceImplTest {

    private CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private CategoryService categoryService = new CategoryServiceImpl(categoryRepository);

    @Test
    public void shouldFindCategoryTreeForAGivenCategoryId(){
        // Given
        final Category categoryOne = new Category(1, "food", "delicious", "img/0005.jpg", 0);
        final Category categoryTwo = new Category(2, "cloth", "delicious", "img/0005.jpg", 1);
        final Category categoryThree = new Category(3, "makeup", "delicious", "img/0005.jpg", 2);
        final Category categoryFour = new Category(4, "kitchen", "delicious", "img/0005.jpg", 3);
        Mockito.when(categoryRepository.findById(1l)).thenReturn(Optional.of(categoryOne));
        Mockito.when(categoryRepository.findById(2l)).thenReturn(Optional.of(categoryTwo));
        Mockito.when(categoryRepository.findById(3l)).thenReturn(Optional.of(categoryThree));
        Mockito.when(categoryRepository.findById(4l)).thenReturn(Optional.of(categoryFour));

        // When & Then
        final List<Category> categoryTreeOne = categoryService.findCategoryTree(4l);
        assertThat(categoryTreeOne.size(), is(4));
        assertThat(categoryTreeOne.get(0), Matchers.is(categoryOne));
        assertThat(categoryTreeOne.get(1), Matchers.is(categoryTwo));
        assertThat(categoryTreeOne.get(2), Matchers.is(categoryThree));
        assertThat(categoryTreeOne.get(3), Matchers.is(categoryFour));

        // When & Then
        final List<Category> categoryTreeTwo = categoryService.findCategoryTree(3l);
        assertThat(categoryTreeTwo.size(), is(3));
        assertThat(categoryTreeTwo.get(0), Matchers.is(categoryOne));
        assertThat(categoryTreeTwo.get(1), Matchers.is(categoryTwo));
        assertThat(categoryTreeTwo.get(2), Matchers.is(categoryThree));

        // When & Then
        final List<Category> categoryTreeThree = categoryService.findCategoryTree(2l);
        assertThat(categoryTreeThree.size(), is(2));
        assertThat(categoryTreeThree.get(0), Matchers.is(categoryOne));
        assertThat(categoryTreeThree.get(1), Matchers.is(categoryTwo));

        // When & Then
        final List<Category> categoryTreeFour = categoryService.findCategoryTree(1l);
        assertThat(categoryTreeFour.size(), is(1));
        assertThat(categoryTreeThree.get(0), Matchers.is(categoryOne));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfCategoryIsNotFound(){
        // When
        when(categoryRepository.findById(1l)).thenReturn(Optional.<Category>empty());

        // Then & When
        categoryService.findCategoryTree(1l);
    }

}