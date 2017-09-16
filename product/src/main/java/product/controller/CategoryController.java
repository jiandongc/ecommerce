package product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product.data.CategoryData;
import product.domain.Category;
import product.domain.Product;
import product.mapper.CategoryDataMapper;
import product.service.CategoryService;
import product.service.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jiandong on 13/11/16.
 */

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryDataMapper categoryDataMapper;

    @Autowired
    public CategoryController(ProductService productService,
                              CategoryService categoryService,
                              CategoryDataMapper categoryDataMapper){
        this.productService = productService;
        this.categoryService = categoryService;
        this.categoryDataMapper = categoryDataMapper;
    }

    @RequestMapping(value = "/{code}", method= RequestMethod.GET)
    public CategoryData findCategoryByCode(@PathVariable String code) {
        final Optional<Category> category = categoryService.findByCode(code);
        if(category.isPresent()){
            final List<Product> products = productService.findByCategoryCode(code);
            final Map<Category, Integer> subCategories = productService.findProductTotalInSubCategories(code);
            final List<Category> parentCategories = categoryService.findParentCategories(code);
            return categoryDataMapper.getValue(category.get(), parentCategories, subCategories, products);
        } else {
            return null;
        }
    }
}
