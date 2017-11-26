package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import product.domain.Category;
import product.domain.Key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 05/11/16.
 */
public class CategoryRepositoryTest extends AbstractRepositoryTest {

    private static final String category_filter_attribute_insert_sql
            = "insert into category_filter_attribute(category_id, attribute_id, ordering) values(?,?,?)";
    private static final String category_insert_sql
            = "insert into category(name, description, image_url, hidden, parent_id, category_code) values (?, ?, ?, ?, ?, ?)";
    private static final String attribute_insert_sql
            = "insert into attribute(name) values (?)";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldFindCategoryByCode(){
        // Given
        jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
        jdbcTemplate.update(attribute_insert_sql, "color");
        jdbcTemplate.update(attribute_insert_sql, "brand");
        final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
        final Long colorKeyId = jdbcTemplate.queryForObject("select id from attribute where name = 'color'", Long.class);
        final Long brandKeyId = jdbcTemplate.queryForObject("select id from attribute where name = 'brand'", Long.class);
        jdbcTemplate.update(category_filter_attribute_insert_sql, categoryId, colorKeyId, 0);
        jdbcTemplate.update(category_filter_attribute_insert_sql, categoryId, brandKeyId, 1);

        // When
        final Category actualCategory = categoryRepository.findByCode("FD").get();

        // Then
        final Key color = new Key();
        color.setName("color");

        final Key brand = new Key();
        brand.setName("brand");

        final Category category = new Category();
        category.setHidden(false);
        category.setName("food");
        category.setDescription("delicious");
        category.setImageUrl("img/0001.jpg");
        category.setCode("FD");
        category.setFilterKeys(Arrays.asList(color, brand));

        assertThat(actualCategory, is(category));
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
        final Category parentCategory = new Category();
        parentCategory.setHidden(false);
        parentCategory.setName("food");
        parentCategory.setDescription("delicious");
        parentCategory.setImageUrl("img/0001.jpg");
        parentCategory.setCode("FD");
        parentCategory.setFilterKeys(new ArrayList<>());

        final Category subCategory1 = new Category();
        subCategory1.setName("crackers");
        subCategory1.setCode("CR");
        subCategory1.setParent(parentCategory);
        subCategory1.setFilterKeys(new ArrayList<>());

        final Category subCategory2 = new Category();
        subCategory2.setName("cake");
        subCategory2.setCode("CA");
        subCategory2.setParent(parentCategory);
        subCategory2.setFilterKeys(new ArrayList<>());

        final Category subCategory3 = new Category();
        subCategory3.setName("chocolate");
        subCategory3.setCode("CH");
        subCategory3.setParent(parentCategory);
        subCategory3.setFilterKeys(new ArrayList<>());

        assertThat(subCategories.size(), is(3));
        assertThat(subCategories, hasItems(subCategory1, subCategory2, subCategory3));
    }

}
