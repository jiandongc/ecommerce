package product.controller;

import static java.math.BigDecimal.*;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;

public class ProductControllerTest extends AbstractControllerTest {

	private static final String BASE_URL = "http://localhost:8083/products/";
	private final TestRestTemplate rest = new TestRestTemplate();

	private Category category;
	private Category anotherCategory;
	private Brand brand;
	private Image image;
	private Image image1;
	private Image image2;
	private Sku sku;
	private Sku sku2;
	private Vat vat;

	@Before
	public void setUp(){
		category = new Category();
		category.setHidden(false);
		category.setName("food");
		category.setDescription("delicious");
		category.setImageUrl("img/0001.jpg");
		category.setCode("FD");
		category.setStartDate(LocalDate.now());
		category.addCategoryAttribute(CategoryAttribute.builder().key("Color").ordering(0).build());
		categoryRepository.save(category);

		anotherCategory = new Category();
		anotherCategory.setHidden(false);
		anotherCategory.setName("cloth");
		anotherCategory.setDescription("beautiful");
		anotherCategory.setImageUrl("img/0001.jpg");
		anotherCategory.setCode("CL");
		categoryRepository.save(anotherCategory);

		brand = Brand.builder().code("shj").name("ShangHaoJia").build();
		brandRepository.save(brand);

		vat = Vat.builder().name("uk_standard").rate(20).build();
		vatRepository.save(vat);

		image = new Image();
		image.setUrl("img/0002.jpg");
		image.setOrdering(0);

		image1 = new Image();
		image1.setUrl("img/0003.jpg");
		image1.setOrdering(1);

		image2 = new Image();
		image2.setUrl("img/0004.jpg");
		image2.setOrdering(2);

		sku = new Sku();
		sku.addPrice(Price.builder().price(TEN).startDate(LocalDate.now()).build());
		sku.setStockQuantity(100);
		sku.setSku("FD10039403_X");
		sku.addAttribute(SkuAttribute.builder().key("Color").value("Red").build());

		sku2 = new Sku();
		sku2.addPrice(Price.builder().price(BigDecimal.ONE).startDate(LocalDate.now()).build());
		sku2.setStockQuantity(70);
		sku2.setSku("FD10039403_Y");
		sku2.addAttribute(SkuAttribute.builder().key("Color").value("Red").build());
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
		productOne.setStartDate(LocalDate.now());
		productOne.setVat(vat);
		productRepository.save(productOne);

		// When & Then
		final Product p = productRepository.findOne(productOne.getId());
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<ProductData> response = rest.exchange(BASE_URL + p.getCode(), GET, httpEntity, ProductData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().getName(), is("Chester"));
		assertThat(response.getBody().getDescription(), is("Chester description"));
		assertThat(response.getBody().getCategoryCode(), is("FD"));
		assertThat(response.getBody().getPrice().toPlainString(), is("10.00"));
		assertThat(response.getBody().getImages().get(0), is("img/0002.jpg"));
		assertThat(response.getBody().getImages().get(1), is("img/0003.jpg"));
		assertThat(response.getBody().getImages().get(2), is("img/0004.jpg"));
		assertThat(response.getBody().getAttributes().get("Color"), is(new HashSet<>(asList("Red"))));
		assertThat(response.getBody().getVariants().get(0).get("price"), is(10.0d));
		assertThat(response.getBody().getVariants().get(0).get("originalPrice"), is(10.0d));
		assertThat(response.getBody().getVariants().get(0).get("isOnSale"), is(false));
		assertThat(response.getBody().getVariants().get(0).get("discountRate"), is(nullValue()));
		assertThat(response.getBody().getVariants().get(0).get("sku"), is("FD10039403_X"));
		assertThat(response.getBody().getVariants().get(0).get("Color"), is("Red"));
		assertThat(response.getBody().getVariants().get(0).get("qty"), is(100));
		assertThat(response.getBody().getVariants().get(0).get("description"), is("Color: Red"));
		assertThat(response.getBody().getBrand().get("name"), is("ShangHaoJia"));
		assertThat(response.getBody().getBrand().get("code"), is("shj"));
	}

	@Test
	public void shouldNotReturnInactiveProduct(){
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
		productOne.setStartDate(LocalDate.now().plusDays(10));
		productRepository.save(productOne);

		// When & Then
		final Product p = productRepository.findOne(productOne.getId());
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		final ResponseEntity<ProductData> response = rest.exchange(BASE_URL + p.getCode(), GET, httpEntity, ProductData.class);
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
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
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.addSku(sku2);
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);

		final Product productThree = new Product();
		productThree.setName("Cloth");
		productThree.setDescription("Cloth description");
		productThree.setCategory(anotherCategory);
		productThree.setStartDate(LocalDate.now());
		productRepository.save(productThree);

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?cc=FD", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getName(), is("Chester"));
		assertThat(response.getBody()[0].getCode(), startsWith("FD"));
		assertThat(response.getBody()[0].getImageUrl(), startsWith("img/0002.jpg"));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("10.00"));
		assertThat(response.getBody()[1].getName(), is("Shoes"));
		assertThat(response.getBody()[1].getCode(), startsWith("FD"));
	}

	@Test
	public void shouldOnlyReturnActiveProductsInCategory(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addImage(image);
		productOne.addSku(sku);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.addSku(sku2);
		productTwo.setStartDate(LocalDate.now().minusDays(10));
		productTwo.setEndDate(LocalDate.now().minusDays(5));
		productRepository.save(productTwo);

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?cc=FD", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));
		assertThat(response.getBody()[0].getName(), is("Chester"));
		assertThat(response.getBody()[0].getCode(), startsWith("FD"));
		assertThat(response.getBody()[0].getImageUrl(), startsWith("img/0002.jpg"));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("10.00"));
	}

	@Test
	public void shouldFindProductsByBrand(){
		// Given
		Brand nike = Brand.builder().code("nike").name("Nike").build();
		brandRepository.save(nike);
		Brand adidas = Brand.builder().code("adidas").name("Adidas").build();
		brandRepository.save(adidas);

		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.setBrand(nike);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.setBrand(nike);
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);

		final Product productThree = new Product();
		productThree.setName("Cloth");
		productThree.setDescription("Cloth description");
		productThree.setCategory(category);
		productThree.setBrand(adidas);
		productThree.setStartDate(LocalDate.now());
		productRepository.save(productThree);

		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?br=nike", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));

		response = rest.exchange(BASE_URL + "?br=adidas", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));

		response = rest.exchange(BASE_URL + "?br=puma", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(0));
	}

	@Test
	public void shouldOnlyReturnActiveProductsInBrand(){
		// Given
		Brand nike = Brand.builder().code("nike").name("Nike").build();
		brandRepository.save(nike);

		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.setBrand(nike);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.setBrand(nike);
		productTwo.setStartDate(LocalDate.now().minusDays(10));
		productTwo.setEndDate(LocalDate.now().minusDays(5));
		productRepository.save(productTwo);


		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?br=nike", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));
	}

	@Test
	public void shouldFindProductByTag(){
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addTag(ProductTag.builder().tag("SALE").startDate(LocalDate.now()).build());
		productOne.addTag(ProductTag.builder().tag("POPULAR").startDate(LocalDate.now()).build());
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.addTag(ProductTag.builder().tag("SALE").startDate(LocalDate.now()).build());
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);

		final Product productThree = new Product();
		productThree.setName("Cloth");
		productThree.setDescription("Cloth description");
		productThree.setCategory(category);
		productThree.addTag(ProductTag.builder().tag("SEASON").startDate(LocalDate.now()).build());
		productThree.setStartDate(LocalDate.now());
		productRepository.save(productThree);

		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?tg=sale", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));

		response = rest.exchange(BASE_URL + "?tg=popular", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));
		assertThat(response.getBody()[0].getName(), is("Chester"));

		response = rest.exchange(BASE_URL + "?tg=season", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));
		assertThat(response.getBody()[0].getName(), is("Cloth"));

		response = rest.exchange(BASE_URL + "?tg=sale&tg=season", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(3));

		response = rest.exchange(BASE_URL + "?tg=new", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(0));
	}

	@Test
	public void shouldOnlyReturnActiveProductsInTag(){
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addTag(ProductTag.builder().tag("SALE").startDate(LocalDate.now()).build());
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.addTag(ProductTag.builder().tag("SALE").startDate(LocalDate.now()).build());
		productTwo.setStartDate(LocalDate.now().plusDays(10));
		productRepository.save(productTwo);


		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?tg=sale", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));
	}

	@Test
	public void shouldOrderProductByPrice(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addImage(image);
		productOne.addSku(sku);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.addSku(sku2);
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?sort=price.asc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("1.00"));
		assertThat(response.getBody()[1].getPrice().toPlainString(), is("10.00"));

		response = rest.exchange(BASE_URL + "?sort=price.desc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(2));
		assertThat(response.getBody()[0].getPrice().toPlainString(), is("10.00"));
		assertThat(response.getBody()[1].getPrice().toPlainString(), is("1.00"));
	}
	
	@Test
	public void shouldFindProductsByMultipleAttributes(){
		// Given
		Brand nike = Brand.builder().code("nike").name("Nike").build();
		brandRepository.save(nike);

		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.addTag(ProductTag.builder().tag("SALE").startDate(LocalDate.now()).build());
		productOne.addTag(ProductTag.builder().tag("POPULAR").startDate(LocalDate.now()).build());
		productOne.addSku(sku);
		productOne.setBrand(nike);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		// When & Then
		final HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<ProductSimpleData[]> response = rest.exchange(BASE_URL + "?cc=FD&tg=sale&tg=popular&br=nike&sort=price.asc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));

		response = rest.exchange(BASE_URL + "?cc=FD&tg=sale&br=nike&sort=price.asc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));

		response = rest.exchange(BASE_URL + "?cc=FD&tg=popular&br=nike&sort=price.asc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(1));

		response = rest.exchange(BASE_URL + "?cc=FD&tg=abc&br=nike&sort=price.asc", GET, httpEntity, ProductSimpleData[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody().length, is(0));
	}

	@Test
	public void shouldFindColorVariant(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("Chester description");
		productOne.setCategory(category);
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);
		productGroupRepository.addColorVariant(1, productOne.getId());

		final Product productTwo = new Product();
		productTwo.setName("Shoes");
		productTwo.setDescription("Shoes description");
		productTwo.setCategory(category);
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);
		productGroupRepository.addColorVariant(1, productTwo.getId());

		final Product productThree = new Product();
		productThree.setName("Tea");
		productThree.setDescription("English Tea");
		productThree.setCategory(category);
		productThree.setStartDate(LocalDate.now());
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
		productOne.addAttribute(ProductAttribute.builder().key("Color").value("Red").build());
		productOne.setStartDate(LocalDate.now());
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
		productOne.setStartDate(LocalDate.now());
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Book");
		productTwo.setDescription("Good book");
		productTwo.setCategory(category);
		productTwo.addSku(sku2); // 1.0
		productTwo.setStartDate(LocalDate.now());
		productRepository.save(productTwo);

		final Product productThree = new Product();
		productThree.setName("Inactive");
		productThree.setCategory(category);
		productThree.setStartDate(LocalDate.now().plusDays(10));
		productRepository.save(productThree);

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