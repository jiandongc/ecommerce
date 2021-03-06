package product.service;

import org.hamcrest.Matchers;
import org.junit.Test;
import product.domain.Category;
import product.mapper.CategoryDataMapper;
import product.repository.CategoryRepository;

import java.time.LocalDate;
import java.util.Arrays;
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

    private ProductService productService = mock(ProductService.class);
    private CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private CategoryDataMapper categoryDataMapper = mock(CategoryDataMapper.class);
    private CategoryService categoryService = new CategoryServiceImpl(productService, categoryRepository, categoryDataMapper);

    @Test
    public void shouldFindParentCategoriesByCode(){
        // Given
        final Category categoryOne = new Category();
        categoryOne.setCode("C1");
        categoryOne.setStartDate(LocalDate.now().minusDays(10));
        final Category categoryTwo = new Category();
        categoryTwo.setCode("C2");
        categoryTwo.setParent(categoryOne);
        categoryTwo.setStartDate(LocalDate.now().minusDays(10));
        final Category categoryThree = new Category();
        categoryThree.setCode("C3");
        categoryThree.setParent(categoryTwo);
        categoryThree.setStartDate(LocalDate.now().minusDays(10));
        final Category categoryFour = new Category();
        categoryFour.setCode("C4");
        categoryFour.setParent(categoryThree);
        categoryFour.setStartDate(LocalDate.now().minusDays(10));
        when(categoryRepository.findByCode("C1")).thenReturn(Optional.of(categoryOne));
        when(categoryRepository.findByCode("C2")).thenReturn(Optional.of(categoryTwo));
        when(categoryRepository.findByCode("C3")).thenReturn(Optional.of(categoryThree));
        when(categoryRepository.findByCode("C4")).thenReturn(Optional.of(categoryFour));

        // When & Then
        final List<Category> parentCategoriesOne = categoryService.findParentCategories("C4");
        assertThat(parentCategoriesOne.size(), is(4));
        assertThat(parentCategoriesOne.get(0), Matchers.is(categoryOne));
        assertThat(parentCategoriesOne.get(1), Matchers.is(categoryTwo));
        assertThat(parentCategoriesOne.get(2), Matchers.is(categoryThree));
        assertThat(parentCategoriesOne.get(3), Matchers.is(categoryFour));

        // When & Then
        final List<Category> parentCategoriesTwo = categoryService.findParentCategories("C3");
        assertThat(parentCategoriesTwo.size(), is(3));
        assertThat(parentCategoriesTwo.get(0), Matchers.is(categoryOne));
        assertThat(parentCategoriesTwo.get(1), Matchers.is(categoryTwo));
        assertThat(parentCategoriesOne.get(2), Matchers.is(categoryThree));

        // When & Then
        final List<Category> parentCategoriesThree = categoryService.findParentCategories("C2");
        assertThat(parentCategoriesThree.size(), is(2));
        assertThat(parentCategoriesThree.get(0), Matchers.is(categoryOne));
        assertThat(parentCategoriesTwo.get(1), Matchers.is(categoryTwo));

        // When & Then
        final List<Category> parentCategoriesFour = categoryService.findParentCategories("C1");
        assertThat(parentCategoriesFour.size(), is(1));
        assertThat(parentCategoriesTwo.get(1), Matchers.is(categoryTwo));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfCategoryIsNotFound(){
        // When
        when(categoryRepository.findByCode("C1")).thenReturn(Optional.<Category>empty());

        // Then & When
        categoryService.findParentCategories("C1");
    }

    @Test
    public void shouldReturnOptionalEmptyIfCategoryIsNotActive(){
        // Given
        final Category category = new Category();
        category.setStartDate(LocalDate.now().plusDays(10));
        when(categoryRepository.findByCode("code")).thenReturn(Optional.of(category));

        // When
        Optional<Category> categoryOptional = categoryService.findByCode("code");

        // Then
        assertThat(categoryOptional.isPresent(), is(false));
    }

    @Test
    public void shouldReturnCategoryOptionalIfIsActive(){
        // Given
        final Category category = new Category();
        category.setStartDate(LocalDate.now().minusDays(10));
        when(categoryRepository.findByCode("code")).thenReturn(Optional.of(category));

        // When
        Optional<Category> categoryOptional = categoryService.findByCode("code");

        // Then
        assertThat(categoryOptional.isPresent(), is(true));
    }

    @Test
    public void shouldOnlyReturnActiveSubCategories(){
        // Given
        final Category categoryOne = new Category();
        categoryOne.setCode("C1");
        categoryOne.setStartDate(LocalDate.now().minusDays(10));
        final Category categoryTwo = new Category();
        categoryTwo.setCode("C2");
        categoryTwo.setStartDate(LocalDate.now());
        final Category categoryThree = new Category();
        categoryThree.setCode("C3");
        categoryThree.setStartDate(LocalDate.now().plusDays(10));
        when(categoryRepository.findSubCategoriesByCode("code")).thenReturn(Arrays.asList(categoryOne, categoryTwo, categoryThree));

        // When
        List<Category> actual = categoryService.findSubCategories("code");

        // Then
        assertThat(actual.size(), is(2));

    }

}