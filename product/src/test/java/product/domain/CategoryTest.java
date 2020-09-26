package product.domain;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CategoryTest {

    @Test
    public void shouldReturnTrueIfCategoryIsActive(){
        // Given
        Category category = new Category();
        category.setStartDate(LocalDate.now());

        assertThat(category.isActive(), is(true));

        category = new Category();
        category.setStartDate(LocalDate.now().minusDays(10));

        assertThat(category.isActive(), is(true));

        category = new Category();
        category.setStartDate(LocalDate.now());
        category.setEndDate(LocalDate.now().plusDays(10));

        assertThat(category.isActive(), is(true));

        category = new Category();
        category.setStartDate(LocalDate.now().minusDays(10));
        category.setEndDate(LocalDate.now().plusDays(10));

        assertThat(category.isActive(), is(true));

        category = new Category();
        category.setStartDate(LocalDate.now().minusDays(10));
        category.setEndDate(LocalDate.now());

        assertThat(category.isActive(), is(true));
    }

    @Test
    public void shouldReturnFalseIfCategoryIsInactive(){
        // Given
        Category category = new Category();
        category.setStartDate(LocalDate.now().plusDays(10));

        assertThat(category.isActive(), is(false));

        category = new Category();
        category.setStartDate(LocalDate.now().minusDays(10));
        category.setEndDate(LocalDate.now().minusDays(1));

        assertThat(category.isActive(), is(false));

        category = new Category();
        category.setStartDate(null);
        category.setEndDate(null);

        assertThat(category.isActive(), is(false));

    }

}