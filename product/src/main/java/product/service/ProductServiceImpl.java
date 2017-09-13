package product.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import product.domain.Category;
import product.domain.Product;
import product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryCode(String categoryCode) {
        final List<Product> products = this.productRepository.findByCategoryCode(categoryCode);
        final List<Category> subCategories = this.categoryService.findSubCategories(categoryCode);
        final List<Product> subCategoriesProducts = subCategories.stream()
                .map(subCategory -> this.findByCategoryCode(subCategory.getCode()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return Stream.concat(products.stream(), subCategoriesProducts.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Category, Integer> findProductTotalInSubCategories(String categoryCode) {
        final List<Category> subCategories = this.categoryService.findSubCategories(categoryCode);
        final Map<Category, Integer> categories = new LinkedHashMap<>();
        for(Category category : subCategories){
            final List<Product> products = this.findByCategoryCode(category.getCode());
            categories.put(category, products.size());
        }
        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }
}
