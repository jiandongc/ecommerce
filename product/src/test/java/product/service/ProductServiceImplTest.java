package product.service;

import static java.lang.Long.valueOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import product.repository.ProductRepository;

public class ProductServiceImplTest {
	private ProductRepository productRepository = mock(ProductRepository.class);
	private ProductSerivce productService = new ProductServiceImpl(productRepository);

	@Test
	public void shouldFindProductById(){
		// Given 
		final long id = valueOf(2);
		// When
		productService.findById(id);
		// Then
		verify(productRepository).findOne(id);
	}
	
	@Test
	public void shouldFindAllProducts(){
		// Given When 
		productService.findAll();
		// Then
		verify(productRepository).findAll();
	}

}
