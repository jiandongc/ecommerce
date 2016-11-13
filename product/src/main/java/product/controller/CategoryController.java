package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product.data.CategorySummaryData;
import product.domain.Category;
import product.domain.Product;
import product.mapper.CategorySummaryDataMapper;
import product.service.CategoryService;
import product.service.ProductService;

import java.util.List;
import java.util.Map;

/**
 * Created by jiandong on 13/11/16.
 */

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategorySummaryDataMapper categorySummaryDataMapper;

    @Autowired
    public CategoryController(ProductService productService,
                              CategoryService categoryService,
                              CategorySummaryDataMapper categorySummaryDataMapper){
        this.productService = productService;
        this.categoryService = categoryService;
        this.categorySummaryDataMapper = categorySummaryDataMapper;
    }

    @RequestMapping(value = "/{id}", method= RequestMethod.GET)
    public CategorySummaryData findCategorySummary(@PathVariable long id) {
        final List<Product> products = productService.findByCategoryId(id);
        final Map<Category, Integer> subCategories = productService.findProductTotalInSubCategories(id);
        final List<Category> categoryTree = categoryService.findCategoryTree(id);
        return categorySummaryDataMapper.getValue(products, subCategories, categoryTree);
    }
}
