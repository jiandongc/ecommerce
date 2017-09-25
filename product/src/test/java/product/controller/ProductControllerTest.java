package product.controller;

import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import product.data.ProductData;
import product.data.ProductSimpleData;
import product.domain.*;

import java.util.Arrays;
import java.util.HashSet;


public class ProductControllerTest extends AbstractControllerTest {

	private static final String BASE_URL = "http://localhost:8083/products/";
	private final TestRestTemplate rest = new TestRestTemplate();

	private Category category;
	private Brand brand;
	private ImageType imageType;
	private ImageType imageType2;
	private Image image;
	private Image image1;
	private Image image2;
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
		image.setOrdering(1);

		imageType2 = new ImageType();
		imageType2.setType("thumbnail");
		imageTypeRepository.save(imageType2);

		image1 = new Image();
		image1.setImageType(imageType2);
		image1.setUrl("img/0003.jpg");
		image1.setOrdering(1);

		image2 = new Image();
		image2.setImageType(imageType2);
		image2.setUrl("img/0004.jpg");
		image2.setOrdering(2);

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
	public void shouldGetProductByCode(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.setBrand(brand);
		productOne.addImage(image);
		productOne.addImage(image1);
		productOne.addImage(image2);
		productOne.addSku(sku);
		productRepository.save(productOne);

		// When & Then
		final Product p = productRepository.findOne(productOne.getId());
		final ResponseEntity<ProductData> response = rest.getForEntity(BASE_URL + p.getCode(), ProductData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Chester"));
		assertThat(response.getBody().getDescription(), is("Chester description"));
		assertThat(response.getBody().getCategoryCode(), is("FD"));
		assertThat(response.getBody().getPrice().toPlainString(), is("10"));
		assertThat(response.getBody().getImages().get("Main").get(0), is("img/0002.jpg"));
		assertThat(response.getBody().getImages().get("thumbnail").get(0), is("img/0003.jpg"));
		assertThat(response.getBody().getImages().get("thumbnail").get(1), is("img/0004.jpg"));
		assertThat(response.getBody().getAttributes().get("Color"), is(new HashSet<>(Arrays.asList("Red"))));
		assertThat(response.getBody().getVariants().get(0).get("price"), is("10"));
		assertThat(response.getBody().getVariants().get(0).get("sku"), is("FD10039403_X"));
		assertThat(response.getBody().getVariants().get(0).get("Color"), is("Red"));
		assertThat(response.getBody().getVariants().get(0).get("qty"), is("100"));
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
		assertThat(response.getBody()[0].getCode(), startsWith("FD"));
		assertThat(response.getBody()[0].getImageUrl(), startsWith("img/0002.jpg"));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("10"));
		assertThat(response.getBody()[1].getName(), is("Shoes"));
		assertThat(response.getBody()[1].getCode(), startsWith("FD"));
	}
}
