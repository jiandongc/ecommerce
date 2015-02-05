package product.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static product.domain.Category.PUFFED_SNACKS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import product.domain.Product;

public class ProductRepositoryTest extends AbstractRepositoryTest{

	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void shouldFindProductById(){
		// Given
		final Product product = new Product("Chester", Double.valueOf(10), "delicious", PUFFED_SNACKS, "img/0001.jpg");
		productRepository.save(product);
		
		// When
		final Product actualProduct = productRepository.findOne(product.getId());
		
		// Then
		assertThat(product, is(actualProduct));
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
		final List<Product> products = productRepository.findAll();
		
		// Then
		assertThat(products, hasItems(productOne, productTwo, productThree));
	}
}
