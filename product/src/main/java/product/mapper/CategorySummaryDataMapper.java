package product.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import product.data.BrandData;
import product.data.CategoryData;
import product.data.CategorySummaryData;
import product.data.ProductData;
import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jiandong on 13/11/16.
 */

@Component
public class CategorySummaryDataMapper {

    private final BrandDataMapper brandDataMapper;
    private final ProductDataMapper productDataMapper;
    private final CategoryDataMapper categoryDataMapper;

    @Autowired
    public CategorySummaryDataMapper(BrandDataMapper brandDataMapper,
                                     ProductDataMapper productDataMapper,
                                     CategoryDataMapper categoryDataMapper) {
        this.brandDataMapper = brandDataMapper;
        this.productDataMapper = productDataMapper;
        this.categoryDataMapper = categoryDataMapper;
    }

    public CategorySummaryData getValue(List<Product> products,
                                        Map<Category, Integer> subCategories,
                                        List<Category> categories){
        int productCount = 0;
        final Set<BrandData> brandDataSet = new HashSet<>();
        final Set<ProductData> productDataSet = new HashSet<>();
        final Set<CategoryData> subCategoryDataSet = new HashSet<>();

        for(Product product : products){
            productCount = productCount + 1;

            final Brand brand = product.getBrand();
            if(brand != null){
                brandDataSet.add(brandDataMapper.getValue(brand));
            }

            productDataSet.add(productDataMapper.getValue(product));
        }

        subCategories.forEach((k,v)->{
            final CategoryData categoryData = new CategoryData(k.getId(), k.getName(), v);
            subCategoryDataSet.add(categoryData);
        });

        final List<CategoryData> parentCategories = categories.stream().map(new Function<Category, CategoryData>() {
            @Override
            public CategoryData apply(Category category) {
                return categoryDataMapper.getValue(category);
            }
        }).collect(Collectors.toList());


        return new CategorySummaryData(productDataSet, subCategoryDataSet, brandDataSet, parentCategories, productCount);
    }
}
