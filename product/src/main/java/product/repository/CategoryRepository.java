package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by jiandong on 03/11/16.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    List<Category> findByParentId(Long parentId);
}
