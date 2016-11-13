package product.service;

import static java.lang.Long.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Test;

import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;
import product.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductServiceImplTest {
	private ProductRepository productRepository = mock(ProductRepository.class);
	private CategoryService categoryService = mock(CategoryService.class);
	private ProductService productService = new ProductServiceImpl(productRepository, categoryService);

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

	/*
							category
			categoryOne					categoryTwo
			productOne			  productTwo    productThree
	*/
	@Test
	public void shouldFindProductsByCategoryIdScenarioOne(){
		// Given
		final Brand brand = new Brand(1, "Walkers");
		final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
		final Category subCategoryOne = new Category(2, "subCategoryOne", "delicious", "img/0005.jpg", 1);
		final Category subCategoryTwo = new Category(3, "subCategoryTwo", "delicious", "img/0005.jpg", 1);
		when(categoryService.findById(1l)).thenReturn(Optional.of(category));
		when(categoryService.findById(2l)).thenReturn(Optional.of(subCategoryOne));
		when(categoryService.findById(3l)).thenReturn(Optional.of(subCategoryTwo));
		when(categoryService.findSubCategoriesByParentId(1l)).thenReturn(asList(subCategoryOne, subCategoryTwo));
		when(categoryService.findSubCategoriesByParentId(2l)).thenReturn(emptyList());
		when(categoryService.findSubCategoriesByParentId(3l)).thenReturn(emptyList());

		final Product productOne = new Product("Chester1", 10d, "delicious", category, brand, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", 10d, "delicious", category, brand, "img/0002.jpg");
		final Product productThree = new Product("Chester3", 10d, "delicious", category, brand, "img/0003.jpg");
		when(productRepository.findByCategoryId(1l)).thenReturn(emptyList());
		when(productRepository.findByCategoryId(2l)).thenReturn(asList(productOne));
		when(productRepository.findByCategoryId(3l)).thenReturn(asList(productTwo, productThree));

		// When
		final List<Product> actualProducts = productService.findByCategoryId(1l);

		// Then
		assertThat(actualProducts.size(), is(3));
		assertThat(actualProducts, hasItems(productOne, productTwo, productThree));
	}

	/*
                        					category
        				categoryOne							        categoryTwo
        categoryThree      			 CategoryFour			  	productOne/productTwo
 productThree/productFour productFive/productSix/productSeven

	*/
	@Test
	public void shouldFindProductsByCategoryIdScenarioTwo(){
		// Given
		final Brand brand = new Brand(1, "Walkers");
		final Category category = new Category(1, "food", "delicious", "img/0005.jpg", 0);
		final Category subCategoryOne = new Category(2, "subCategoryOne", "delicious", "img/0005.jpg", 1);
		final Category subCategoryTwo = new Category(3, "subCategoryTwo", "delicious", "img/0005.jpg", 1);
		final Category subCategoryThree = new Category(4, "subCategoryOne", "delicious", "img/0005.jpg", 2);
		final Category subCategoryFour = new Category(5, "subCategoryOne", "delicious", "img/0005.jpg", 2);
		when(categoryService.findById(1l)).thenReturn(Optional.of(category));
		when(categoryService.findById(2l)).thenReturn(Optional.of(subCategoryOne));
		when(categoryService.findById(3l)).thenReturn(Optional.of(subCategoryTwo));
		when(categoryService.findById(4l)).thenReturn(Optional.of(subCategoryThree));
		when(categoryService.findById(5l)).thenReturn(Optional.of(subCategoryFour));
		when(categoryService.findSubCategoriesByParentId(1l)).thenReturn(asList(subCategoryOne, subCategoryTwo));
		when(categoryService.findSubCategoriesByParentId(2l)).thenReturn(asList(subCategoryThree, subCategoryFour));
		when(categoryService.findSubCategoriesByParentId(3l)).thenReturn(emptyList());
		when(categoryService.findSubCategoriesByParentId(4l)).thenReturn(emptyList());
		when(categoryService.findSubCategoriesByParentId(5l)).thenReturn(emptyList());

		final Product productOne = new Product("Chester1", 10d, "delicious", category, brand, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", 10d, "delicious", category, brand, "img/0002.jpg");
		final Product productThree = new Product("Chester3", 10d, "delicious", category, brand, "img/0003.jpg");
		final Product productFour = new Product("Chester4", 10d, "delicious", category, brand, "img/0004.jpg");
		final Product productFive = new Product("Chester5", 10d, "delicious", category, brand, "img/0005.jpg");
		final Product productSix = new Product("Chester6", 10d, "delicious", category, brand, "img/0006.jpg");
		final Product productSeven = new Product("Chester7", 10d, "delicious", category, brand, "img/0007.jpg");
		when(productRepository.findByCategoryId(1l)).thenReturn(emptyList());
		when(productRepository.findByCategoryId(2l)).thenReturn(emptyList());
		when(productRepository.findByCategoryId(3l)).thenReturn(asList(productOne, productTwo));
		when(productRepository.findByCategoryId(4l)).thenReturn(asList(productThree, productFour));
		when(productRepository.findByCategoryId(5l)).thenReturn(asList(productFive, productSix, productSeven));

		// When & Then
		final List<Product> actualProducts = productService.findByCategoryId(1l);
		assertThat(actualProducts.size(), is(7));
		assertThat(actualProducts, hasItems(productOne, productTwo, productThree, productThree, productFour, productFive, productSix, productSeven));

		// When & Then
		final List<Product> actualProducts2 = productService.findByCategoryId(2l);
		assertThat(actualProducts2.size(), is(5));
		assertThat(actualProducts2, hasItems(productThree, productThree, productFour, productFive, productSix, productSeven));

		// When & Then
		final List<Product> actualProducts3 = productService.findByCategoryId(3l);
		assertThat(actualProducts3.size(), is(2));
		assertThat(actualProducts3, hasItems(productOne, productTwo));

		// When & Then
		final List<Product> actualProducts4 = productService.findByCategoryId(4l);
		assertThat(actualProducts4.size(), is(2));
		assertThat(actualProducts4, hasItems(productThree, productFour));

		// When & Then
		final List<Product> actualProducts5 = productService.findByCategoryId(5l);
		assertThat(actualProducts5.size(), is(3));
		assertThat(actualProducts5, hasItems(productFive, productSix, productSeven));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionIfCategoryIdIsNotValid(){
		// When
		when(categoryService.findById(3l)).thenReturn(Optional.<Category>empty());

		// Then & When
		productService.findByCategoryId(3l);
	}

	@Test
	public void shouldReturnProductTotalInSubCategories(){
		// Given
		final Brand brandOne = new Brand(1, "Walkers");

		final Category categoryOne = new Category(2, "food", "delicious", "img/0005.jpg", 0);
		final Category categoryTwo = new Category(3, "cloth", "beautiful", "img/0006.jpg", 0);
		when(categoryService.findSubCategoriesByParentId(1l)).thenReturn(asList(categoryOne, categoryTwo));
		when(categoryService.findById(2l)).thenReturn(Optional.of(categoryOne));
		when(categoryService.findById(3l)).thenReturn(Optional.of(categoryTwo));
		when(categoryService.findSubCategoriesByParentId(2l)).thenReturn(emptyList());
		when(categoryService.findSubCategoriesByParentId(3l)).thenReturn(emptyList());

		final Product productOne = new Product("Chester1", 10d, "delicious", categoryOne, brandOne, "img/0001.jpg");
		final Product productTwo = new Product("Chester2", 10d, "delicious", categoryOne, brandOne, "img/0002.jpg");
		final Product productThree = new Product("Chester3", 10d, "delicious", categoryOne, brandOne, "img/0003.jpg");
		final Product productFour = new Product("Chester4", 10d, "delicious", categoryTwo, brandOne, "img/0004.jpg");
		final Product productFive = new Product("Chester5", 10d, "delicious", categoryTwo, brandOne, "img/0005.jpg");
		when(productRepository.findByCategoryId(2l)).thenReturn(asList(productOne, productTwo, productThree));
		when(productRepository.findByCategoryId(3l)).thenReturn(asList(productFour, productFive));

		// When
		final Map<Category, Integer> actual = productService.findProductTotalInSubCategories(1l);

		// Then
		assertThat(actual.get(categoryOne), Matchers.is(3));
		assertThat(actual.get(categoryTwo), Matchers.is(2));
	}


}
