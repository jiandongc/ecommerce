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
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findSubCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    @Override
    public List<Category> findCategoryTree(Long id) {
        final Optional<Category> categoryOptional = this.findById(id);
        final Category category = categoryOptional.orElseThrow(() -> new IllegalArgumentException(format("Category Id : %s not found.", id)));

        if (!category.isTopCategory()) {
            long parentId = category.getParentId();
            final List<Category> categoryTree = findCategoryTree(parentId);
            categoryTree.add(category);
            return categoryTree;
        } else {
            final ArrayList<Category> categoryTree = new ArrayList<>();
            categoryTree.add(category);
            return categoryTree;
        }
    }
}
