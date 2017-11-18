package product.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
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

	private static final String category_insert_sql
			= "insert into category(name, description, image_url, hidden, parent_id, category_code) values (?, ?, ?, ?, ?, ?)";
	private static final String attribute_insert_sql
			= "insert into attribute(name) values (?)";
	private static final String attribute_value_insert_sql
			= "insert into attribute_value(attribute_id, attribute_value) values(?, ?)";
	private static final String image_type_insert_sql
			= "insert into image_type(type) values (?)";
	private static final String image_insert_sql
			= "insert into product_image(product_id, image_type_id, url, ordering) values (?, ?, ?, ?)";
	private static final String product_insert_sql
			= "insert into product(category_id, description, name) values (?, ?, ?)";
	private static final String sku_insert_sql
			= "insert into sku(product_id, sku, stock_quantity, price) values (?, ?, ?, ?)";
	private static final String sku_attribute_value_insert_sql
			= "insert into sku_attribute_value (sku_id, attribute_value_id) values (?, ?)";
	private static final String product_attribute_value_insert_sql
			= "insert into product_attribute_value(product_id, attribute_value_id) values (?, ?)";
	private static final String product_group_insert_sql
			= "insert into product.product_group(product_group, type, product_id) values (?, ?, ?)";

	@Test
	public void shouldSaveAndFindItByProductCode(){
		// Given
		jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
		jdbcTemplate.update(attribute_insert_sql, "color");
		final Long colorAttributeId = jdbcTemplate.queryForObject("select id from attribute where name = 'color'", Long.class);
		jdbcTemplate.update(attribute_value_insert_sql,colorAttributeId, "red");
		jdbcTemplate.update(attribute_insert_sql, "brand");
		final Long brandAttributeId = jdbcTemplate.queryForObject("select id from attribute where name = 'brand'", Long.class);
		jdbcTemplate.update(attribute_value_insert_sql,brandAttributeId, "nike");
		jdbcTemplate.update(image_type_insert_sql, "thumbnail");
		final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester");
		final Long productId = jdbcTemplate.queryForObject("select id from product where name = 'chester'", Long.class);
		final Long imageTypeId = jdbcTemplate.queryForObject("select id from image_type where type = 'thumbnail'", Long.class);
		jdbcTemplate.update(image_insert_sql, productId, imageTypeId, "img/0001.jpg", 1);
		jdbcTemplate.update(image_insert_sql, productId, imageTypeId, "img/0003.jpg", 3);
		jdbcTemplate.update(image_insert_sql, productId, imageTypeId, "img/0002.jpg", 2);
		jdbcTemplate.update(sku_insert_sql, productId, "FD10039403_X", 100, 10);
		final Long skuId = jdbcTemplate.queryForObject("select id from sku where sku = 'FD10039403_X'", Long.class);
		final Long colorAttributeValueId = jdbcTemplate.queryForObject("select av.id " +
				"from attribute_value av join attribute a on a.id = av.attribute_id " +
				"where a.name = 'color'", Long.class);
		final Long brandAttributeValueId = jdbcTemplate.queryForObject("select av.id " +
				"from attribute_value av join attribute a on a.id = av.attribute_id " +
				"where a.name = 'brand'", Long.class);
		jdbcTemplate.update(sku_attribute_value_insert_sql, skuId, colorAttributeValueId);
		jdbcTemplate.update(sku_attribute_value_insert_sql, skuId, brandAttributeValueId);
		jdbcTemplate.update(product_attribute_value_insert_sql, productId, colorAttributeValueId);
		jdbcTemplate.update(product_attribute_value_insert_sql, productId, brandAttributeValueId);

		// When
		final String productCode = jdbcTemplate.queryForObject("select product_code from product where name = 'chester'", String.class);
		final Product actualProduct = productRepository.findByCode(productCode).get();

		// Then
		assertThat(actualProduct.getCode(), is(productCode));
		assertThat(actualProduct.getName(), is("chester"));
		assertThat(actualProduct.getDescription(), is("delicious"));

		assertThat(actualProduct.getCategory().getCode(), is("FD"));
		assertThat(actualProduct.getCategory().getName(), is("food"));
		assertThat(actualProduct.getCategory().getDescription(), is("delicious"));
		assertThat(actualProduct.getCategory().getImageUrl(), is("img/0001.jpg"));
		assertThat(actualProduct.getCategory().isHidden(), is(false));

		assertThat(actualProduct.getImages().get(0).getUrl(), is("img/0001.jpg"));
		assertThat(actualProduct.getImages().get(0).getOrdering(), is(1));
		assertThat(actualProduct.getImages().get(0).getImageType().getType(), is("thumbnail"));
		assertThat(actualProduct.getImages().get(1).getUrl(), is("img/0002.jpg"));
		assertThat(actualProduct.getImages().get(1).getOrdering(), is(2));
		assertThat(actualProduct.getImages().get(1).getImageType().getType(), is("thumbnail"));
		assertThat(actualProduct.getImages().get(2).getUrl(), is("img/0003.jpg"));
		assertThat(actualProduct.getImages().get(2).getOrdering(), is(3));
		assertThat(actualProduct.getImages().get(2).getImageType().getType(), is("thumbnail"));

		assertThat(actualProduct.getSkus().get(0).getPrice(), is(BigDecimal.TEN));
		assertThat(actualProduct.getSkus().get(0).getStockQuantity(), is(100));
		assertThat(actualProduct.getSkus().get(0).getSku(), is("FD10039403_X"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(0).getKeyName(), is("color"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(0).getValue(), is("red"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(1).getKeyName(), is("brand"));
		assertThat(actualProduct.getSkus().get(0).getAttributes().get(1).getValue(), is("nike"));

		assertThat(actualProduct.getAttributes().get(0).getKeyName(), is("color"));
		assertThat(actualProduct.getAttributes().get(0).getValue(), is("red"));
		assertThat(actualProduct.getAttributes().get(1).getKeyName(), is("brand"));
		assertThat(actualProduct.getAttributes().get(1).getValue(), is("nike"));
	}

	@Test
	public void shouldFindProductsByCategoryCode(){
		// Given
		jdbcTemplate.update(category_insert_sql, "food", "delicious", "img/0001.jpg", false, null, "FD");
		final Long categoryId = jdbcTemplate.queryForObject("select id from category where name = 'food'", Long.class);
		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester");
		jdbcTemplate.update(product_insert_sql,categoryId, "yummy", "coke");
		jdbcTemplate.update(product_insert_sql,categoryId, "fat", "cake");

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

		jdbcTemplate.update(product_insert_sql,categoryId, "delicious", "chester");
		final Long productOneId = jdbcTemplate.queryForObject("select id from product where name = 'chester'", Long.class);
		jdbcTemplate.update(product_group_insert_sql, 1, "Color", productOneId);

		jdbcTemplate.update(product_insert_sql,categoryId, "yummy", "coke");
		final Long productTwoId = jdbcTemplate.queryForObject("select id from product where name = 'coke'", Long.class);
		jdbcTemplate.update(product_group_insert_sql, 1, "Color", productTwoId);

		jdbcTemplate.update(product_insert_sql,categoryId, "fat", "cake");
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
