package product.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import org.springframework.jdbc.core.JdbcTemplate;
import product.domain.*;

public class ProductRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProductRepository productRepository;

	private static final String brand_insert_sql
			= "insert into brand(name, code) values (?, ?)";
	private static final String category_insert_sql
			= "insert into category(name, description, image_url, hidden, parent_id, category_code) values (?, ?, ?, ?, ?, ?)";
	private static final String image_insert_sql
			= "insert into product_image(product_id, url, ordering) values (?, ?, ?)";
	private static final String product_insert_sql
			= "insert into product(category_id, description, name, brand_id) values (?, ?, ?, ?)";
	private static final String sku_insert_sql
			= "insert into sku(product_id, sku, stock_quantity) values (?, ?, ?)";
	private static final String sku_attribute_insert_sql
			= "insert into sku_attribute (sku_id, key, value) values (?, ?, ?)";
	private static final String sku_price_insert_sql
			= "insert into price(sku_id, price, start_date, end_date, discount_rate) values (?, ?, ?, ?, ?)";
	private static final String product_attribute_insert_sql
			= "insert into product_attribute(product_id, key, value) values (?, ?, ?)";
	private static final String product_tag_insert_sql
			= "insert into product_tag(product_id, tag, color_hex, start_date, end_date) values (?, ?, ?, ?, ?)";
	private static final String product_group_insert_sql
			= "insert into product.product_group(product_group, type, product_id) values (?, ?, ?)";

	@Test
	public void shouldSaveAndFindItByProductCode(){
		// Given
		jdbcTemplate.update(brand_insert_sql, "shanghaojia", "shj");
		final Long brandId = jdbcTemplate.queryForObject("select id from brand where code = 'shj'", Long.class);
		jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
		final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester", brandId);
		final Long productId = jdbcTemplate.queryForObject("select id from product where name = 'chester'", Long.class);
		jdbcTemplate.update(image_insert_sql, productId, "img/0001.jpg", 1);
		jdbcTemplate.update(image_insert_sql, productId, "img/0003.jpg", 3);
		jdbcTemplate.update(image_insert_sql, productId, "img/0002.jpg", 2);
		jdbcTemplate.update(sku_insert_sql, productId, "FD10039403_X", 100);
		final Long skuId = jdbcTemplate.queryForObject("select id from sku where sku = 'FD10039403_X'", Long.class);
		jdbcTemplate.update(sku_price_insert_sql, skuId, 10, Date.valueOf("2019-11-01"), null, null);
		jdbcTemplate.update(sku_price_insert_sql, skuId, 9, Date.valueOf("2019-11-01"), Date.valueOf("2019-12-01"), "10%");
		jdbcTemplate.update(sku_attribute_insert_sql, skuId, "color", "red");
		jdbcTemplate.update(sku_attribute_insert_sql, skuId, "brand", "nike");
		jdbcTemplate.update(product_attribute_insert_sql, productId, "color", "red");
		jdbcTemplate.update(product_attribute_insert_sql, productId, "brand", "nike");
		jdbcTemplate.update(product_tag_insert_sql, productId, "new", "#F0C14B", Date.valueOf("2019-11-01"), Date.valueOf("2019-12-01"));
		jdbcTemplate.update(product_tag_insert_sql, productId, "sale", null, Date.valueOf("2020-11-01"), Date.valueOf("2020-12-01"));

		// When
		final String productCode = jdbcTemplate.queryForObject("select product_code from product where name = 'chester'", String.class);
		final Product actualProduct = productRepository.findByCode(productCode).get();

		// Then
		assertThat(actualProduct.getCode(), is(productCode));
		assertThat(actualProduct.getName(), is("chester"));
		assertThat(actualProduct.getDescription(), is("delicious"));

		assertThat(actualProduct.getBrand().getName(), is("shanghaojia"));
		assertThat(actualProduct.getBrand().getCode(), is("shj"));

		assertThat(actualProduct.getCategory().getCode(), is("FD"));
		assertThat(actualProduct.getCategory().getName(), is("food"));
		assertThat(actualProduct.getCategory().getDescription(), is("delicious"));
		assertThat(actualProduct.getCategory().getImageUrl(), is("img/0001.jpg"));
		assertThat(actualProduct.getCategory().isHidden(), is(false));

		assertThat(actualProduct.getImages().get(0).getUrl(), is("img/0001.jpg"));
		assertThat(actualProduct.getImages().get(0).getOrdering(), is(1));
		assertThat(actualProduct.getImages().get(1).getUrl(), is("img/0002.jpg"));
		assertThat(actualProduct.getImages().get(1).getOrdering(), is(2));
		assertThat(actualProduct.getImages().get(2).getUrl(), is("img/0003.jpg"));
		assertThat(actualProduct.getImages().get(2).getOrdering(), is(3));

		assertThat(actualProduct.getSkus().get(0).getStockQuantity(), is(100));
		assertThat(actualProduct.getSkus().get(0).getSku(), is("FD10039403_X"));
		assertThat(actualProduct.getSkus().get(0).getPrices().size(), is(2));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(0).getPrice(), is(BigDecimal.valueOf(10)));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(0).getStartDate(), is(LocalDate.of(2019, 11, 1)));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(0).getEndDate(), is(nullValue()));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(0).getDiscountRate(), is(nullValue()));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(1).getPrice(), is(BigDecimal.valueOf(9)));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(1).getStartDate(), is(LocalDate.of(2019, 11, 1)));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(1).getEndDate(), is(LocalDate.of(2019, 12, 1)));
		assertThat(actualProduct.getSkus().get(0).getPrices().get(1).getDiscountRate(), is("10%"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(0).getKey(), is("color"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(0).getValue(), is("red"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(1).getKey(), is("brand"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(1).getValue(), is("nike"));

		assertThat(actualProduct.getAttributes().get(0).getKey(), is("color"));
		assertThat(actualProduct.getAttributes().get(0).getValue(), is("red"));
		assertThat(actualProduct.getAttributes().get(1).getKey(), is("brand"));
		assertThat(actualProduct.getAttributes().get(1).getValue(), is("nike"));

		assertThat(actualProduct.getTags().get(0).getTag(), is("new"));
		assertThat(actualProduct.getTags().get(0).getColorHex(), is("#F0C14B"));
		assertThat(actualProduct.getTags().get(0).getStartDate(), is(LocalDate.of(2019, 11, 1)));
		assertThat(actualProduct.getTags().get(0).getEndDate(), is(LocalDate.of(2019, 12, 1)));

		assertThat(actualProduct.getTags().get(1).getTag(), is("sale"));
		assertThat(actualProduct.getTags().get(1).getColorHex(), is(nullValue()));
		assertThat(actualProduct.getTags().get(1).getStartDate(), is(LocalDate.of(2020, 11, 1)));
		assertThat(actualProduct.getTags().get(1).getEndDate(), is(LocalDate.of(2020, 12, 1)));
	}

	@Test
	public void shouldFindProductsByCategoryCode(){
		// Given
		jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
		final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester", null);
		jdbcTemplate.update(product_insert_sql,categoryId, "yummy", "coke", null);
		jdbcTemplate.update(product_insert_sql,categoryId, "fat", "cake", null);

		// When
		final List<Product> products = productRepository.findByCategoryCode("FD");

		// Then
		assertThat(products.size(), is(3));
		assertThat(products.get(0).getName(), is("chester"));
		assertThat(products.get(1).getName(), is("coke"));
		assertThat(products.get(2).getName(), is("cake"));
	}

	@Test
	public void shouldFindProductColorVariantIds(){
		// Given
		jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
		final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);

		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester", null);
		final Long productOneId = jdbcTemplate.queryForObject("select id from product where name = 'chester'", Long.class);
		jdbcTemplate.update(product_group_insert_sql, 1, "Color", productOneId);

		jdbcTemplate.update(product_insert_sql,categoryId, "yummy", "coke", null);
		final Long productTwoId = jdbcTemplate.queryForObject("select id from product where name = 'coke'", Long.class);
		jdbcTemplate.update(product_group_insert_sql, 1, "Color", productTwoId);

		jdbcTemplate.update(product_insert_sql,categoryId, "fat", "cake", null);
		final Long productThreeId = jdbcTemplate.queryForObject("select id from product where name = 'cake'", Long.class);
		jdbcTemplate.update(product_group_insert_sql, 1, "Color", productThreeId);


		// When & Then
		final List<Integer> idsOne = productRepository.findColorVariantIds(productOneId);
		assertThat(idsOne, hasSize(2));
		assertThat(idsOne, hasItems(productTwoId.intValue(), productThreeId.intValue()));

		// When & Then
		final List<Integer> idsTwo = productRepository.findColorVariantIds(productTwoId);
		assertThat(idsTwo, hasSize(2));
		assertThat(idsTwo, hasItems(productOneId.intValue(), productThreeId.intValue()));

		// When & Then
		final List<Integer> idsThree = productRepository.findColorVariantIds(productThreeId);
		assertThat(idsThree, hasSize(2));
		assertThat(idsThree, hasItems(productOneId.intValue(), productTwoId.intValue()));
	}

}
