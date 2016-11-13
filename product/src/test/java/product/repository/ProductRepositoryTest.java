package product.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;

public class ProductRepositoryTest extends AbstractRepositoryTest{

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Before
	public void setUp(){
		final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
		categoryRepository.save(category);
		final Brand brand = new Brand(1, "Walkers");
		brandRepository.save(brand);
	}

	@Test
	public void shouldFindProductById(){
		// Given
		final Category category = categoryRepository.findOne(1l);
		final Brand brand = brandRepository.findOne(1l);
		final Product product = new Product("Chester", 10d, "delicious", category, brand, "img/0001.jpg");
		productRepository.save(product);
		
		// When
		final Product actualProduct = productRepository.findOne(product.getId());
		
		// Then
		assertThat(product, is(actualProduct));
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
		final List<Product> products = productRepository.findAll();
		
		// Then
		assertThat(products, hasItems(productOne, productTwo, productThree));
	}

	@Test
	public void shouldFindProductsByCategoryId(){
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
		final List<Product> products = productRepository.findByCategoryId(1l);

		// Then
		assertThat(products, hasItems(productOne, productTwo, productThree));
	}
}
