package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.data.CategoryData;
import product.domain.Category;
import product.domain.Product;
import product.mapper.CategoryDataMapper;
import product.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Created by jiandong on 05/11/16.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final CategoryDataMapper categoryDataMapper;

    @Autowired
    public CategoryServiceImpl(@Lazy ProductService productService,
                               CategoryRepository categoryRepository,
                               CategoryDataMapper categoryDataMapper) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.categoryDataMapper = categoryDataMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByCode(String code) {
        return categoryRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findSubCategories(String code) {
        return categoryRepository.findSubCategoriesByCode(code);
    }

    /* return all parent categories + current category */
    @Override
    @Transactional(readOnly = true)
    public List<Category> findParentCategories(String code) {
        final Optional<Category> categoryOptional = this.findByCode(code);
        final Category category = categoryOptional.orElseThrow(() -> new IllegalArgumentException(format("Category Code : %s not found.", code)));

        if (!category.isTopCategory()) {
            final List<Category> categories = this.findParentCategories(category.getParent().getCode());
            categories.add(category);
            return categories;
        } else {
            final ArrayList<Category> categories = new ArrayList<>();
            categories.add(category);
            return categories;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryData> getCategoryData(String code) {
        final Optional<Category> category = this.findByCode(code);
        if (category.isPresent()) {
            final List<Product> products = productService.findByCategoryCode(code);
            final Map<Category, Integer> subCategories = productService.findProductTotalInSubCategories(code);
            final List<Category> parentCategories = this.findParentCategories(code);
            final CategoryData categoryData = categoryDataMapper.getValue(category.get(), parentCategories, subCategories, products);
            return Optional.of(categoryData);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryData> findSubCategories(String code, int level) {
        final Optional<Category> category = this.findByCode(code);
        if (category.isPresent() && level == 0) {
            return Optional.of(categoryDataMapper.getValue(category.get()));
        } else if (category.isPresent() && level > 0) {
            final List<Category> subCategories = this.findSubCategories(code);
            final List<CategoryData> categoryDataList = new ArrayList<>();
            subCategories.forEach(subCategory -> {
                Optional<CategoryData> subCategoryData = this.findSubCategories(subCategory.getCode(), level - 1);
                subCategoryData.ifPresent(categoryDataList::add);
            });
            return Optional.of(CategoryData.builder()
                            .code(category.get().getCode())
                            .name(category.get().getName())
                            .children(categoryDataList.isEmpty() ? null : categoryDataList)
                            .build());
        } else {
            return Optional.empty();
        }
    }
}
