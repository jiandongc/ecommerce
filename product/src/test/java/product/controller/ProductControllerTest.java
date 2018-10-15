package product.controller;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.HttpMethod.GET;

import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import product.data.ProductData;
import product.data.ProductSearchData;
import product.data.ProductSimpleData;
import product.domain.*;

import java.util.HashSet;

public class ProductControllerTest extends AbstractControllerTest {

	private static final String BASE_URL = "http://localhost:8083/products/";
	private final TestRestTemplate rest = new TestRestTemplate();

	private Category category;
	private ImageType imageType;
	private ImageType imageType2;
	private Image image;
	private Image image1;
	private Image image2;
	private Key key;
	private Sku sku;
	private Sku sku2;
	private Attribute attribute;

	@Before
	public void setUp(){
		key = new Key();
		key.setName("Color");
		keyRepository.save(key);

		attribute = new Attribute();
		attribute.setKey(key);
		attribute.setValue("Red");
		attributeRepository.save(attribute);

		category = new Category();
		category.setHidden(false);
		category.setName("food");
		category.setDescription("delicious");
		category.setImageUrl("img/0001.jpg");
		category.setCode("FD");
		category.setFilterKeys(asList(key));
		categoryRepository.save(category);

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

		sku = new Sku();
		sku.setPrice(TEN);
		sku.setStockQuantity(100);
		sku.setSku("FD10039403_X");
		sku.addAttribute(attribute);

		sku2 = new Sku();
		sku2.setPrice(ONE);
		sku2.setStockQuantity(70);
		sku2.setSku("FD10039403_Y");
		sku2.addAttribute(attribute);
	}
	
	@Test
	public void shouldGetProductByCode(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addImage(image);
		productOne.addImage(image1);
		productOne.addImage(image2);
		productOne.addSku(sku);
		productRepository.save(productOne);

		// When & Then
		final Product p = productRepository.findOne(productOne.getId());
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<ProductData> response = rest.exchange(BASE_URL + p.getCode(), GET, httpEntity, ProductData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Chester"));
		assertThat(response.getBody().getDescription(), is("Chester description"));
		assertThat(response.getBody().getCategoryCode(), is("FD"));
		assertThat(response.getBody().getPrice().toPlainString(), is("10"));
		assertThat(response.getBody().getImages().get("Main").get(0), is("img/0002.jpg"));
		assertThat(response.getBody().getImages().get("thumbnail").get(0), is("img/0003.jpg"));
		assertThat(response.getBody().getImages().get("thumbnail").get(1), is("img/0004.jpg"));
		assertThat(response.getBody().getAttributes().get("Color"), is(new HashSet<>(asList("Red"))));
		assertThat(response.getBody().getVariants().get(0).get("price"), is("10"));
		assertThat(response.getBody().getVariants().get(0).get("sku"), is("FD10039403_X"));
		assertThat(response.getBody().getVariants().get(0).get("Color"), is("Red"));
		assertThat(response.getBody().getVariants().get(0).get("qty"), is("100"));
		assertThat(response.getBody().getVariants().get(0).get("description"), is("Color: Red"));
	}

	@Test
	public void shouldReturn404IfProductCodeDoesNotExist(){
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<ProductData> response = rest.exchange(BASE_URL + "12345", GET, httpEntity, ProductData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}

	@Test
	public void shouldFindProductsByCategoryCode(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addImage(image);
		productOne.addSku(sku);
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productRepository.save(productTwo);

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?cc=FD", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getName(), is("Chester"));
		assertThat(response.getBody()[0].getCode(), startsWith("FD"));
		assertThat(response.getBody()[0].getImageUrl(), startsWith("img/0002.jpg"));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("10"));
		assertThat(response.getBody()[1].getName(), is("Shoes"));
		assertThat(response.getBody()[1].getCode(), startsWith("FD"));
	}

	@Test
	public void shouldFindColorVariant(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productRepository.save(productOne);
		productGroupRepository.addColorVariant(1, productOne.getId());

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productRepository.save(productTwo);
		productGroupRepository.addColorVariant(1, productTwo.getId());

		final Product productThree = new Product();
		productThree.setName("Tea");
		productThree.setDescription("English Tea");
		productThree.setCategory(category);
		productRepository.save(productThree);
		productGroupRepository.addColorVariant(1, productThree.getId());

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final Product p1 = productRepository.findOne(productOne.getId());
		final ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "color/" + p1.getCode(), GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getName(), is("Shoes"));
		assertThat(response.getBody()[1].getName(), is("Tea"));

		// When & Then
		final Product p2 = productRepository.findOne(productTwo.getId());
		final ResponseEntity<ProductSimpleData[]> response2 = rest.exchange(BASE_URL + "color/" + p2.getCode(), GET, httpEntity, ProductSimpleData[].class);
		assertThat(response2.getStatusCode(), is(HttpStatus.OK));
		assertThat(response2.getBody().length, is(2));
		assertThat(response2.getBody()[0].getName(), is("Chester"));
		assertThat(response2.getBody()[1].getName(), is("Tea"));

		// When & Then
		final Product p3 = productRepository.findOne(productThree.getId());
		final ResponseEntity<ProductSimpleData[]> response3 = rest.exchange(BASE_URL + "color/" + p3.getCode(), GET, httpEntity, ProductSimpleData[].class);
		assertThat(response3.getStatusCode(), is(HttpStatus.OK));
		assertThat(response3.getBody().length, is(2));
		assertThat(response3.getBody()[0].getName(), is("Chester"));
		assertThat(response3.getBody()[1].getName(), is("Shoes"));
	}

	@Test
	public void shouldFilterProductsInCategory(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addAttribute(attribute);
		productRepository.save(productOne);

		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

		// When & Then
		String filterStr = "{\"color\":[\"red\"]}";
		ResponseEntity<ProductSearchData> response = rest.exchange(BASE_URL + "search/categories/FD?filter={filterStr}", GET, httpEntity, ProductSearchData.class, filterStr);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getProducts().size(), is(1));
		assertThat(response.getBody().getProducts().get(0).getName(), is("Chester"));
		assertThat(response.getBody().getFacets().size(), is(1));
		assertThat(response.getBody().getFacets().get(0).getName(), is("color"));
		assertThat(response.getBody().getFacets().get(0).isHasSelectedValue(), is(true));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().size(), is(1));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).getCount(), is(1));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).getName(), is("red"));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).isSelected(), is(true));


		// When & Then
		response = rest.exchange(BASE_URL + "search/categories/FD", GET, httpEntity, ProductSearchData.class, filterStr);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getProducts().size(), is(1));
		assertThat(response.getBody().getProducts().get(0).getName(), is("Chester"));
		assertThat(response.getBody().getFacets().size(), is(1));
		assertThat(response.getBody().getFacets().get(0).getName(), is("color"));
		assertThat(response.getBody().getFacets().get(0).isHasSelectedValue(), is(false));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().size(), is(1));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).getCount(), is(1));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).getName(), is("red"));
		assertThat(response.getBody().getFacets().get(0).getFacetValues().get(0).isSelected(), is(false));
	}

	@Test
	public void shouldSortProductByPrice(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addSku(sku); // 10.0
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Book");
		productTwo.setDescription("Good book");
		productTwo.setCategory(category);
		productTwo.addSku(sku2); // 1.0
		productRepository.save(productTwo);

		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

		// When & Then
		ResponseEntity<ProductSearchData> response = rest.exchange(BASE_URL + "search/categories/FD?sort=pricedesc", GET, httpEntity, ProductSearchData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getProducts().size(), is(2));
		assertThat(response.getBody().getProducts().get(0).getName(), is("Chester"));
		assertThat(response.getBody().getProducts().get(1).getName(), is("Book"));

		// When & Then
		response = rest.exchange(BASE_URL + "search/categories/FD?sort=priceasc", GET, httpEntity, ProductSearchData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getProducts().size(), is(2));
		assertThat(response.getBody().getProducts().get(0).getName(), is("Book"));
		assertThat(response.getBody().getProducts().get(1).getName(), is("Chester"));
	}
}