package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import product.domain.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by jiandong on 03/11/16.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCode(String code);

    @Query(value = "select * from category c " +
            "join category p on c.parent_id = p.id " +
            "where p.category_code = :categoryCode", nativeQuery = true)
    List<Category> findSubCategoriesByCode(@Param("categoryCode") String categoryCode);
}
