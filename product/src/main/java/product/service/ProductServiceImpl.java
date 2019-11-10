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

import static product.service.ProductPredicate.brandFilter;
import static product.service.ProductPredicate.tagsFilter;

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
        for (Category category : subCategories) {
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

    @Override
    @Transactional(readOnly = true)
    public List<Product> findColorVariant(String code) {
        final Optional<Product> product = this.findByCode(code);
        if (product.isPresent()) {
            final List<Integer> ids = productRepository.findColorVariantIds(product.get().getId());
            return ids.stream().map(id -> productRepository.findOne(Long.valueOf(id))).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProducts(String categoryCode, List<String> tags, String brand, String sort) {
        List<Product> products;
        if (categoryCode == null) {
            products = productRepository.findAll();
        } else {
            products = this.findByCategoryCode(categoryCode);
        }

        if (tags != null) {
            products = products.stream().filter(tagsFilter(tags)).collect(Collectors.toList());
        }

        if (brand != null) {
            products = products.stream().filter(brandFilter(brand)).collect(Collectors.toList());
        }

        if (sort != null && sort.equalsIgnoreCase("price.asc")) {
            products = products.stream().sorted(ProductPredicate.priceAscComparator()).collect(Collectors.toList());
        }

        if (sort != null && sort.equalsIgnoreCase("price.desc")) {
            products = products.stream().sorted(ProductPredicate.priceDescComparator()).collect(Collectors.toList());
        }

        return products;
    }
}
