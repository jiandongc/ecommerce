package product.controller;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import product.data.BrandData;
import product.data.CategoryData;
import product.data.CategorySummaryData;
import product.data.ProductData;
import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;
import product.repository.BrandRepository;
import product.repository.CategoryRepository;
import product.repository.ProductRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryControllerTest extends AbstractControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    private final String BASE_URL = "http://localhost:8083/categories/";
    private final RestTemplate rest = new TestRestTemplate();

    @After
    public void after(){
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @Test
    public void shouldReturnCategorySummaryDataByCategoryId(){
        // Given
        final Brand brandOne = new Brand(1, "Walkers");
        final Brand brandTwo = new Brand(2, "Royal China");
        final Category categoryOne = new Category(1, "food", "delicious", "img/0005.jpg", 0);
        final Category categoryTwo = new Category(2, "cloth", "delicious", "img/0005.jpg", 1);
        final Category categoryThree = new Category(3, "makeup", "delicious", "img/0005.jpg", 1);
        final Category categoryFour = new Category(4, "kitchen", "delicious", "img/0005.jpg", 1);
        final Product productOne = new Product("Chester1", 10d, "delicious", categoryTwo, brandOne, "img/0001.jpg");
        final Product productTwo = new Product("Chester2", 10d, "delicious", categoryThree, brandTwo, "img/0002.jpg");
        final Product productThree = new Product("Chester3", 10d, "delicious", categoryThree, brandTwo, "img/0003.jpg");
        final Product productFour = new Product("Chester4", 10d, "delicious", categoryThree, brandTwo, "img/0004.jpg");
        final Product productFive = new Product("Chester5", 10d, "delicious", categoryThree, brandOne, "img/0005.jpg");
        final Product productSix = new Product("Chester6", 10d, "delicious", categoryFour, brandOne, "img/0006.jpg");
        final Product productSeven = new Product("Chester7", 10d, "delicious", categoryFour, brandOne, "img/0007.jpg");

        brandRepository.save(brandOne);
        brandRepository.save(brandTwo);
        categoryRepository.save(categoryOne);
        categoryRepository.save(categoryTwo);
        categoryRepository.save(categoryThree);
        categoryRepository.save(categoryFour);
        productRepository.save(productOne);
        productRepository.save(productTwo);
        productRepository.save(productThree);
        productRepository.save(productFour);
        productRepository.save(productFive);
        productRepository.save(productSix);
        productRepository.save(productSeven);

        // When
        final ResponseEntity<CategorySummaryData> response = rest.getForEntity(BASE_URL + categoryOne.getId(), CategorySummaryData.class);

        // Then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        final BrandData brandDataOne = new BrandData(1, "Walkers");
        final BrandData brandDataTwo = new BrandData(2, "Royal China");
        assertThat(response.getBody().getBrands().size(), is(2));
        assertThat(response.getBody().getBrands(), hasItems(brandDataOne, brandDataTwo));

        final ProductData productDataOne = new ProductData(productOne.getId(), "Chester1", 10d, "delicious", "cloth", "Walkers", "img/0001.jpg");
        final ProductData productDataTwo = new ProductData(productTwo.getId(), "Chester2", 10d, "delicious", "makeup", "Royal China", "img/0002.jpg");
        final ProductData productDataThree = new ProductData(productThree.getId(), "Chester3", 10d, "delicious", "makeup", "Royal China", "img/0003.jpg");
        final ProductData productDataFour = new ProductData(productFour.getId(), "Chester4", 10d, "delicious", "makeup", "Royal China", "img/0004.jpg");
        final ProductData productDataFive = new ProductData(productFive.getId(), "Chester5", 10d, "delicious", "makeup", "Walkers", "img/0005.jpg");
        final ProductData productDataSix = new ProductData(productSix.getId(), "Chester6", 10d, "delicious", "kitchen", "Walkers", "img/0006.jpg");
        final ProductData productDataSeven = new ProductData(productSeven.getId(), "Chester7", 10d, "delicious", "kitchen", "Walkers", "img/0007.jpg");
        assertThat(response.getBody().getProducts().size(), is(7));
        assertThat(response.getBody().getProducts(), hasItems(productDataOne, productDataTwo, productDataThree, productDataFour, productDataFive, productDataSix, productDataSeven));

        final CategoryData subCategoryOne = new CategoryData(2, "cloth", 1);
        final CategoryData subCategoryTwo = new CategoryData(3, "makeup", 4);
        final CategoryData subCategoryThree = new CategoryData(4, "kitchen", 2);
        assertThat(response.getBody().getSubCategories().size(), is(3));
        assertThat(response.getBody().getSubCategories(), hasItems(subCategoryOne, subCategoryTwo, subCategoryThree));

        final CategoryData currentCategory = new CategoryData(1, "food", 0);
        assertThat(response.getBody().getParentCategories().size(), is(1));
        assertThat(response.getBody().getParentCategories(), hasItems(currentCategory));

        assertThat(response.getBody().getProductCount(), is(7));
        assertThat(response.getBody().getCategoryName(), is("food"));

        // When & Then
        final ResponseEntity<CategorySummaryData> responseTwo = rest.getForEntity(BASE_URL + categoryTwo.getId(), CategorySummaryData.class);
        final CategoryData parentCategory = new CategoryData(1, "food", 0);
        final CategoryData currentCategoryTwo = new CategoryData(2, "cloth", 0);
        assertThat(responseTwo.getBody().getParentCategories().size(), is(2));
        assertThat(responseTwo.getBody().getParentCategories().get(0), is(parentCategory));
        assertThat(responseTwo.getBody().getParentCategories().get(1), is(currentCategoryTwo));
        assertThat(responseTwo.getBody().getCategoryName(), is("cloth"));
    }

}
