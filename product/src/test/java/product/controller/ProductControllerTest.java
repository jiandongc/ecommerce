package product.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;
import product.repository.BrandRepository;
import product.repository.CategoryRepository;
import product.repository.ProductRepository;


public class ProductControllerTest extends AbstractControllerTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BrandRepository brandRepository;

	private final String BASE_URL = "http://localhost:8083/products/";
	private final RestTemplate rest = new TestRestTemplate();

	@Before
	public void setUp(){
		final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
		categoryRepository.save(category);
		final Brand brand = new Brand(1, "Walkers");
		brandRepository.save(brand);
	}

	@After
	public void after(){
		productRepository.deleteAll();
		categoryRepository.deleteAll();
		brandRepository.deleteAll();
	}
	
	@Test
	public void shouldGetProductById(){
		// Given
		final Category category = categoryRepository.findOne(1l);
		final Brand brand = brandRepository.findOne(1l);
		final Product product = new Product("Chester", 10d, "delicious", category, brand, "img/0001.jpg");
		productRepository.save(product);
				
		// When
		final ResponseEntity<Product> response = rest.getForEntity(BASE_URL + product.getId(), Product.class);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), is(product));
	}
	
	@Test
	public void shouldFindAllProducts(){
		// Given
		final Category category = categoryRepository.findOne(1l);
		final Brand brand = brandRepository.findOne(1l);
		final Product productOne = new Product("Chester1", 10d, "delicious", category, brand, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", 10d, "delicious", category, brand, "img/0002.jpg");
		final Product productThree = new Product("Chester3", 10d, "delicious", category, brand, "img/0003.jpg");

		productRepository.save(productOne);
		productRepository.save(productTwo);
		productRepository.save(productThree);
		
		// When
		final ResponseEntity<Product[]> response = rest.getForEntity(BASE_URL, Product[].class);
		
		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(asList(response.getBody()), hasItems(productOne, productTwo, productThree));
	}

	@Test
	public void shouldFindProductsByCategoryId(){
		// Given
		final Category categoryTwo = new Category(2, "food", "delicious", "img/0005.jpg", 1);
		categoryRepository.save(categoryTwo);
		final Category categoryThree = new Category(3, "food", "delicious", "img/0005.jpg", 1);
		categoryRepository.save(categoryThree);
		final Brand brand = brandRepository.findOne(1l);

		final Category subCategoryTwo = categoryRepository.findOne(2l);
		final Product productOne = new Product("Chester1", 10d, "delicious", subCategoryTwo, brand, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", 10d, "delicious", subCategoryTwo, brand, "img/0002.jpg");
		productRepository.save(productOne);
		productRepository.save(productTwo);

		final Category subCategoryThree = categoryRepository.findOne(3l);
		final Product productThree = new Product("Chester3", 10d, "delicious", subCategoryThree, brand, "img/0003.jpg");
		productRepository.save(productThree);

		// When & Then
		final ResponseEntity<Product[]> response = rest.getForEntity(BASE_URL + "?categoryId=1", Product[].class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(asList(response.getBody()), hasItems(productOne, productTwo, productThree));

		// When & Then
		final ResponseEntity<Product[]> response2 = rest.getForEntity(BASE_URL + "?categoryId=2", Product[].class);
		assertThat(response2.getStatusCode(), is(HttpStatus.OK));
		assertThat(asList(response2.getBody()), hasItems(productOne, productTwo));

		// When & Then
		final ResponseEntity<Product[]> response3 = rest.getForEntity(BASE_URL + "?categoryId=3", Product[].class);
		assertThat(response3.getStatusCode(), is(HttpStatus.OK));
		assertThat(asList(response3.getBody()), hasItems(productThree));
	}
}
