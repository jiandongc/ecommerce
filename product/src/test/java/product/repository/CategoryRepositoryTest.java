package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import product.domain.Category;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 05/11/16.
 */
public class CategoryRepositoryTest extends AbstractRepositoryTest {

    private static final String category_insert_sql
            = "insert into category(name, description, image_url, hidden, parent_id, category_code) values (?, ?, ?, ?, ?, ?)";
    private static final String category_attribute_insert_sql
            = "insert into category_attribute(category_id, key, ordering) values (?, ?, ?)";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldFindCategoryByCode(){
        // Given
        jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
        final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
        jdbcTemplate.update(category_attribute_insert_sql, categoryId, "color", 0);
        jdbcTemplate.update(category_attribute_insert_sql, categoryId, "brand", 1);

        // When
        final Category actualCategory = categoryRepository.findByCode("FD").get();

        // Then
        assertThat(actualCategory.getName(), is("food"));
        assertThat(actualCategory.getDescription(), is("delicious"));
        assertThat(actualCategory.getImageUrl(), is("img/0001.jpg"));
        assertThat(actualCategory.getCode(), is("FD"));
        assertThat(actualCategory.isHidden(), is(false));
        assertThat(actualCategory.getCategoryAttributes().size(), is(2));
        assertThat(actualCategory.getCategoryAttributes().get(0).getKey(), is("color"));
        assertThat(actualCategory.getCategoryAttributes().get(1).getKey(), is("brand"));
    }

    @Test
    public void shouldFindSubCategoriesByCode(){
        // Given
        jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
        final Long parentCategoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
        jdbcTemplate.update(category_insert_sql, "crackers", null, null, false, parentCategoryId, "CR");
        jdbcTemplate.update(category_insert_sql, "cake", null, null, false, parentCategoryId, "CA");
        jdbcTemplate.update(category_insert_sql, "chocolate", null, null, false, parentCategoryId, "CH");

        // When
        final List<Category> subCategories = categoryRepository.findSubCategoriesByCode("FD");

        // Then
        assertThat(subCategories.size(), is(3));
        assertThat(subCategories.get(0).getName(), is("crackers"));
        assertThat(subCategories.get(1).getName(), is("cake"));
        assertThat(subCategories.get(2).getName(), is("chocolate"));
    }

}
