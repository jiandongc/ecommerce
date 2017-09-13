package product.service;

import product.domain.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by jiandong on 05/11/16.
 */
public interface CategoryService {

    Optional<Category> findByCode(String code);

    List<Category> findSubCategories(String parentCode);

    List<Category> findParentCategories(String code);
}
