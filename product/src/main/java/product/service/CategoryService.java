package product.service;

import product.domain.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by jiandong on 05/11/16.
 */
public interface CategoryService {

    Optional<Category> findById(Long id);

    List<Category> findSubCategoriesByParentId(Long parentId);

    List<Category> findCategoryTree(Long id);
}
