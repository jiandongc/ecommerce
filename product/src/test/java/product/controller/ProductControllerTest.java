package product.controller;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import product.data.ProductSimpleData;
import product.domain.*;


public class ProductControllerTest extends AbstractControllerTest {

	private static final String BASE_URL = "http://localhost:8083/products/";
	private final TestRestTemplate rest = new TestRestTemplate();

	private Category category;
	private Brand brand;
	private ImageType imageType;
	private Image image;
	private Key key;
	private Sku sku;
	private Attribute attribute;

	@Before
	public void setUp(){
		this.cleanUp();

		category = new Category();
		category.setHidden(false);
		category.setName("food");
		category.setDescription("delicious");
		category.setImageUrl("img/0001.jpg");
		category.setCode("FD");
		categoryRepository.save(category);

		brand = new Brand();
		brand.setName("Walkers");
		brandRepository.save(brand);

		imageType = new ImageType();
		imageType.setType("Main");
		imageTypeRepository.save(imageType);

		image = new Image();
		image.setImageType(imageType);
		image.setUrl("img/0002.jpg");

		key = new Key();
		key.setName("Color");
		keyRepository.save(key);

		attribute = new Attribute();
		attribute.setKey(key);
		attribute.setValue("Red");
		attributeRepository.save(attribute);

		sku = new Sku();
		sku.setPrice(TEN);
		sku.setStockQuantity(100);
		sku.setSku("FD10039403_X");
		sku.addAttribute(attribute);
	}

	@After
	public void after(){
		this.cleanUp();
	}
	
	@Test
	public void shouldGetProductById(){
//		// Given
//		final Category category = categoryRepository.findOne(1l);
//		final Brand brand = brandRepository.findOne(1l);
//		final Product product = new Product("Chester", 10d, "delicious", category, brand, "img/0001.jpg");
//		productRepository.save(product);
//
//		// When
//		final ResponseEntity<Product> response = rest.getForEntity(BASE_URL + product.getId(), Product.class);
//
//		// Then
//		assertThat(response.getStatusCode(), is(HttpStatus.OK));
//		assertThat(response.getBody(), is(product));
	}
	
	@Test
	public void shouldFindAllProducts(){
//		// Given
//		final Category category = categoryRepository.findOne(1l);
//		final Brand brand = brandRepository.findOne(1l);
//		final Product productOne = new Product("Chester1", 10d, "delicious", category, brand, "img/0001.jpg");
//		final Product productTwo = new Product("Chester2", 10d, "delicious", category, brand, "img/0002.jpg");
//		final Product productThree = new Product("Chester3", 10d, "delicious", category, brand, "img/0003.jpg");
//
//		productRepository.save(productOne);
//		productRepository.save(productTwo);
//		productRepository.save(productThree);
//
//		// When
//		final ResponseEntity<Product[]> response = rest.getForEntity(BASE_URL, Product[].class);
//
//		// Then
//		assertThat(response.getStatusCode(), is(HttpStatus.OK));
//		assertThat(asList(response.getBody()), hasItems(productOne, productTwo, productThree));
	}

	@Test
	public void shouldFindProductsByCategoryCode(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.setBrand(brand);
		productOne.addImage(image);
		productOne.addSku(sku);
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productRepository.save(productTwo);

		// When & Then
		final ResponseEntity<ProductSimpleData[]> response = rest.getForEntity(BASE_URL + "?cc=FD", ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getName(), is("Chester"));
		assertThat(response.getBody()[0].getDescription(), is("Chester description"));
		assertThat(response.getBody()[0].getCode(), startsWith("FD"));
		assertThat(response.getBody()[0].getImageUrl(), startsWith("img/0002.jpg"));
		assertThat(response.getBody()[1].getName(), is("Shoes"));
		assertThat(response.getBody()[1].getDescription(), is("Shoes description"));
		assertThat(response.getBody()[1].getCode(), startsWith("FD"));
	}
}
