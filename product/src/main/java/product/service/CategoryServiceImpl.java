package product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product.domain.Category;
import product.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by jiandong on 05/11/16.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findSubCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }
}
