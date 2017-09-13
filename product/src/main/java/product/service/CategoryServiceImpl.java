package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.domain.Category;
import product.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * Created by jiandong on 05/11/16.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByCode(String code) {
        return categoryRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findSubCategories(String parentCode) {
        return categoryRepository.findSubCategoriesByCode(parentCode);
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
}
