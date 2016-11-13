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

import static java.lang.String.format;

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
    public Product findById(long Id) {
        return productRepository.findOne(Id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(long categoryId) {
        final Optional<Category> categoryOptional = categoryService.findById(categoryId);
        final Category category = categoryOptional.orElseThrow(() -> new IllegalArgumentException(format("Category Id : %s not found.", categoryId)));

        final List<Product> products = productRepository.findByCategoryId(category.getId());

        final List<Category> subCategories = this.categoryService.findSubCategoriesByParentId(categoryId);
        final List<Product> subCategoriesProducts = subCategories.stream()
                .map(subCategory -> this.findByCategoryId(subCategory.getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return Stream.concat(products.stream(), subCategoriesProducts.stream())
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public Map<Category, Integer> findProductTotalInSubCategories(Long categoryId) {
        final List<Category> subCategories = categoryService.findSubCategoriesByParentId(categoryId);
        final Map<Category, Integer> categories = new HashMap<>();
        for(Category category : subCategories){
            final List<Product> products = this.findByCategoryId(category.getId());
            categories.put(category, products.size());
        }
        return categories;
    }
}
