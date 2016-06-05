package product.controller;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static product.domain.Category.PUFFED_SNACKS;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import product.domain.Product;
import product.repository.ProductRepository;


public class ProductControllerTest extends AbstractControllerTest{

	@Autowired
	private ProductRepository productRepository;

	private final String BASE_URL = "http://localhost:8083/products/";
	private RestTemplate rest = new TestRestTemplate();

	@After
	public void before(){
		productRepository.deleteAll();
	}
	
	@Test
	public void shouldGetProductById(){
		// Given 
		final Product product = new Product("Chester", Double.valueOf(10), "delicious", PUFFED_SNACKS, "img/0001.jpg");
		productRepository.save(product);
				
		// When
		final ResponseEntity<Product> repsonse = rest.getForEntity(BASE_URL + product.getId(), Product.class);

		// Then
		assertThat(repsonse.getStatusCode(), is(HttpStatus.OK));
		assertThat(repsonse.getBody(), is(product));
	}
	
	@Test
	public void shouldFindAllProducts(){
		// Given
		final Product productOne = new Product("Chester1", Double.valueOf(10), "delicious", PUFFED_SNACKS, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", Double.valueOf(10), "delicious", PUFFED_SNACKS, "img/0002.jpg");
		final Product productThree = new Product("Chester3", Double.valueOf(10), "delicious", PUFFED_SNACKS, "img/0003.jpg");

		productRepository.save(productOne);
		productRepository.save(productTwo);
		productRepository.save(productThree);
		
		// When
		final ResponseEntity<Product[]> repsonse = rest.getForEntity(BASE_URL, Product[].class);
		
		// Then
		assertThat(repsonse.getStatusCode(), is(HttpStatus.OK));
		assertThat(asList(repsonse.getBody()), hasItems(productOne, productTwo, productThree));
	}
}
