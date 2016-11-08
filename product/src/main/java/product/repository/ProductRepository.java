package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import product.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{

    String FIND_PRODUCT_BY_CATEGORY_ID_SQL = "select * from product where categoryId = :categoryId";

    @Query(value = FIND_PRODUCT_BY_CATEGORY_ID_SQL, nativeQuery = true)
    List<Product> findByCategoryId(@Param("categoryId") long categoryId);
}
