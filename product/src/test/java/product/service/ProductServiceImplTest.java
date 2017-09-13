package product.service;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import product.domain.Category;
import product.domain.Product;
import product.repository.ProductRepository;

import java.util.List;
import java.util.Map;

public class ProductServiceImplTest {
	private ProductRepository productRepository = mock(ProductRepository.class);
	private CategoryService categoryService = mock(CategoryService.class);
	private ProductService productService = new ProductServiceImpl(productRepository, categoryService);

	/*
							category
			categoryOne					categoryTwo
			productOne			  productTwo    productThree
	*/
	@Test
	public void shouldFindProductsByCategoryCodeScenarioOne(){
		// Given
		final Category category = new Category();
		category.setCode("FD");
		final Category subCategoryOne = new Category();
		subCategoryOne.setCode("FD1");
		final Category subCategoryTwo = new Category();
		subCategoryTwo.setCode("FD2");
		when(categoryService.findSubCategories("FD")).thenReturn(asList(subCategoryOne, subCategoryTwo));
		when(categoryService.findSubCategories("FD1")).thenReturn(emptyList());
		when(categoryService.findSubCategories("FD2")).thenReturn(emptyList());

		final Product productOne = new Product();
		productOne.setCode("P1");
		final Product productTwo = new Product();
		productTwo.setCode("P2");
		final Product productThree = new Product();
		productThree.setCode("P3");
		when(productRepository.findByCategoryCode("FD")).thenReturn(emptyList());
		when(productRepository.findByCategoryCode("FD1")).thenReturn(asList(productOne));
		when(productRepository.findByCategoryCode("FD2")).thenReturn(asList(productTwo, productThree));

		// When
		final List<Product> actualProducts = productService.findByCategoryCode("FD");

		// Then
		assertThat(actualProducts.size(), is(3));
		assertThat(actualProducts, hasItems(productOne, productTwo, productThree));
	}

	/*
                        					Category
        				CategoryOne							        CategoryTwo
        CategoryThree     p8/p9		 CategoryFour			  	productOne/productTwo
 productThree/productFour productFive/productSix/productSeven
	*/
	@Test
	public void shouldFindProductsByCategoryIdScenarioTwo(){
		// Given
		final Category category = new Category();
		category.setCode("FD");
		final Category subCategoryOne = new Category();
		subCategoryOne.setCode("FD1");
		final Category subCategoryTwo = new Category();
		subCategoryTwo.setCode("FD2");
		final Category subCategoryThree = new Category();
		subCategoryThree.setCode("FD3");
		final Category subCategoryFour = new Category();
		subCategoryFour.setCode("FD4");
		when(categoryService.findSubCategories("FD")).thenReturn(asList(subCategoryOne, subCategoryTwo));
		when(categoryService.findSubCategories("FD1")).thenReturn(asList(subCategoryThree, subCategoryFour));
		when(categoryService.findSubCategories("FD2")).thenReturn(emptyList());
		when(categoryService.findSubCategories("FD3")).thenReturn(emptyList());
		when(categoryService.findSubCategories("FD4")).thenReturn(emptyList());

		final Product productOne = new Product();
		productOne.setCode("P1");
		final Product productTwo = new Product();
		productTwo.setCode("P2");
		final Product productThree = new Product();
		productThree.setCode("P3");
		final Product productFour = new Product();
		productFour.setCode("P4");
		final Product productFive = new Product();
		productFive.setCode("P5");
		final Product productSix = new Product();
		productSix.setCode("P6");
		final Product productSeven = new Product();
		productSeven.setCode("P7");
		final Product productEight = new Product();
		productEight.setCode("P8");
		final Product productNine = new Product();
		productNine.setCode("P9");
		when(productRepository.findByCategoryCode("FD")).thenReturn(emptyList());
		when(productRepository.findByCategoryCode("FD1")).thenReturn(asList(productEight, productNine));
		when(productRepository.findByCategoryCode("FD2")).thenReturn(asList(productOne, productTwo));
		when(productRepository.findByCategoryCode("FD3")).thenReturn(asList(productThree, productFour));
		when(productRepository.findByCategoryCode("FD4")).thenReturn(asList(productFive, productSix, productSeven));

		// When & Then
		final List<Product> actualProducts = productService.findByCategoryCode("FD");
		assertThat(actualProducts.size(), is(9));
		assertThat(actualProducts, hasItems(productOne, productTwo, productThree, productFour, productFive, productSix, productSeven, productEight, productNine));

		// When & Then
		final List<Product> actualProducts2 = productService.findByCategoryCode("FD1");
		assertThat(actualProducts2.size(), is(7));
		assertThat(actualProducts2, hasItems(productThree, productFour, productFive, productSix, productSeven, productEight, productNine));

		// When & Then
		final List<Product> actualProducts3 = productService.findByCategoryCode("FD2");
		assertThat(actualProducts3.size(), is(2));
		assertThat(actualProducts3, hasItems(productOne, productTwo));

		// When & Then
		final List<Product> actualProducts4 = productService.findByCategoryCode("FD3");
		assertThat(actualProducts4.size(), is(2));
		assertThat(actualProducts4, hasItems(productThree, productFour));

		// When & Then
		final List<Product> actualProducts5 = productService.findByCategoryCode("FD4");
		assertThat(actualProducts5.size(), is(3));
		assertThat(actualProducts5, hasItems(productFive, productSix, productSeven));
	}

	/*
                    CategoryOne
    CategoryTwo       p1/p2		 CategoryThree
      p3/p4                        p5/p6/p7
    */
	@Test
	public void shouldReturnProductTotalInSubCategories(){
		// Given
		final Category categoryOne = new Category();
		categoryOne.setCode("C1");
		final Category categoryTwo = new Category();
		categoryTwo.setCode("C2");
		final Category categoryThree = new Category();
		categoryThree.setCode("C3");
		when(categoryService.findSubCategories("C1")).thenReturn(asList(categoryTwo, categoryThree));
		when(categoryService.findSubCategories("C2")).thenReturn(emptyList());
		when(categoryService.findSubCategories("C3")).thenReturn(emptyList());

		final Product p1 = new Product();
		p1.setCode("p1");
		final Product p2 = new Product();
		p2.setCode("p2");
		final Product p3 = new Product();
		p3.setCode("p3");
		final Product p4 = new Product();
		p4.setCode("p4");
		final Product p5 = new Product();
		p5.setCode("p5");
		final Product p6 = new Product();
		p6.setCode("p6");
		final Product p7 = new Product();
		p7.setCode("p7");
		when(productRepository.findByCategoryCode("C1")).thenReturn(asList(p1, p2));
		when(productRepository.findByCategoryCode("C2")).thenReturn(asList(p3, p4));
		when(productRepository.findByCategoryCode("C3")).thenReturn(asList(p5, p6, p7));

		// When & Then
		final Map<Category, Integer> c1Actual = productService.findProductTotalInSubCategories("C1");
		assertThat(c1Actual.get(categoryTwo), is(2));
		assertThat(c1Actual.get(categoryThree), is(3));

		// When & Then
		final Map<Category, Integer> c2Actual = productService.findProductTotalInSubCategories("C2");
		assertThat(c2Actual.isEmpty(), is(true));

		// When & Then
		final Map<Category, Integer> c3Actual = productService.findProductTotalInSubCategories("C3");
		assertThat(c3Actual.isEmpty(), is(true));
	}


}
