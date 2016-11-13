package product.mapper;

import org.junit.Test;
import product.data.BrandData;
import product.data.CategoryData;
import product.data.CategorySummaryData;
import product.data.ProductData;
import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategorySummaryDataMapperTest {

    private final BrandDataMapper brandDataMapper = new BrandDataMapper();
    private final ProductDataMapper productDataMapper = new ProductDataMapper();
    private final CategoryDataMapper categoryDataMapper = new CategoryDataMapper();
    private final CategorySummaryDataMapper mapper = new CategorySummaryDataMapper(brandDataMapper, productDataMapper, categoryDataMapper);

    @Test
    public void shouldMapProductListToCategorySummaryData(){
        // Given
        final Brand brandOne = new Brand(1, "Walkers");
        final Brand brandTwo = new Brand(2, "Royal China");
        final Category categoryOne = new Category(1, "food", "delicious", "img/0005.jpg", 0);
        final Category categoryTwo = new Category(2, "cloth", "delicious", "img/0005.jpg", 1);
        final Category categoryThree = new Category(3, "makeup", "delicious", "img/0005.jpg", 1);
        final Product productOne = new Product("Chester1", 10d, "delicious", categoryOne, brandOne, "img/0001.jpg");
        final Product productTwo = new Product("Chester2", 10d, "delicious", categoryTwo, brandTwo, "img/0002.jpg");
        final Product productThree = new Product("Chester3", 10d, "delicious", categoryTwo, brandTwo, "img/0003.jpg");
        final Product productFour = new Product("Chester4", 10d, "delicious", categoryThree, brandTwo, "img/0004.jpg");
        final Product productFive = new Product("Chester5", 10d, "delicious", categoryThree, brandOne, "img/0005.jpg");
        final Product productSix = new Product("Chester6", 10d, "delicious", categoryThree, brandOne, "img/0006.jpg");
        final Product productSeven = new Product("Chester7", 10d, "delicious", categoryThree, brandOne, "img/0007.jpg");

        final Map<Category, Integer> subCategories = new HashMap<>();
        subCategories.put(categoryOne, 1);
        subCategories.put(categoryTwo, 2);
        subCategories.put(categoryThree, 4);

        final List<Category> categoryTree = asList(categoryOne, categoryTwo, categoryThree);

        // When
        final CategorySummaryData categorySummaryData =
                mapper.getValue(
                        asList(productOne, productTwo, productThree, productFour, productFive, productSix, productSeven),
                        subCategories,
                        categoryTree);

        // Then
        final BrandData brandDataOne = new BrandData(1, "Walkers");
        final BrandData brandDataTwo = new BrandData(2, "Royal China");
        assertThat(categorySummaryData.getBrands().size(), is(2));
        assertThat(categorySummaryData.getBrands(), hasItems(brandDataOne, brandDataTwo));

        final ProductData productDataOne = new ProductData(productOne.getId(), "Chester1", 10d, "delicious", "food", "Walkers", "img/0001.jpg");
        final ProductData productDataTwo = new ProductData(productTwo.getId(), "Chester2", 10d, "delicious", "cloth", "Royal China", "img/0002.jpg");
        final ProductData productDataThree = new ProductData(productThree.getId(), "Chester3", 10d, "delicious", "cloth", "Royal China", "img/0003.jpg");
        final ProductData productDataFour = new ProductData(productFour.getId(), "Chester4", 10d, "delicious", "makeup", "Royal China", "img/0004.jpg");
        final ProductData productDataFive = new ProductData(productFive.getId(), "Chester5", 10d, "delicious", "makeup", "Walkers", "img/0005.jpg");
        final ProductData productDataSix = new ProductData(productSix.getId(), "Chester6", 10d, "delicious", "makeup", "Walkers", "img/0006.jpg");
        final ProductData productDataSeven = new ProductData(productSeven.getId(), "Chester7", 10d, "delicious", "makeup", "Walkers", "img/0007.jpg");
        assertThat(categorySummaryData.getProducts().size(), is(7));
        assertThat(categorySummaryData.getProducts(), hasItems(productDataOne, productDataTwo, productDataThree, productDataFour, productDataFive, productDataSix, productDataSeven));

        final CategoryData subCategoryOne = new CategoryData(1, "food", 1);
        final CategoryData subCategoryTwo = new CategoryData(2, "cloth", 2);
        final CategoryData subCategoryThree = new CategoryData(3, "makeup", 4);
        assertThat(categorySummaryData.getSubCategories().size(), is(3));
        assertThat(categorySummaryData.getSubCategories(), hasItems(subCategoryOne, subCategoryTwo, subCategoryThree));

        assertThat(categorySummaryData.getProductCount(), is(7));

        final CategoryData categoryTreeOne = new CategoryData(1, "food", 0);
        final CategoryData categoryTreeTwo = new CategoryData(2, "cloth", 0);
        final CategoryData categoryTreeThree = new CategoryData(3, "makeup", 0);
        assertThat(categorySummaryData.getParentCategories().size(), is(3));
        assertThat(categorySummaryData.getParentCategories().get(0), is(categoryTreeOne));
        assertThat(categorySummaryData.getParentCategories().get(1), is(categoryTreeTwo));
        assertThat(categorySummaryData.getParentCategories().get(2), is(categoryTreeThree));

        assertThat(categorySummaryData.getCategoryName(), is("makeup"));
    }


}