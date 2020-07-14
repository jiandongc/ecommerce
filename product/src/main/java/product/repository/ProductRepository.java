package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query(value = "select * from product p " +
            "join category c on p.category_id = c.id " +
            "where c.category_code = :categoryCode", nativeQuery = true)
    List<Product> findByCategoryCode(@Param("categoryCode") String categoryCode);

    Optional<Product> findByCode(String code);

    @Query(value = "SELECT g2.product_id " +
            "FROM product_group g1 " +
            "JOIN product_group g2 ON g1.product_group = g2.product_group AND g1.product_id <> g2.product_id " +
            "WHERE UPPER(g1.type) = :type " +
            "AND g1.product_id = :productId", nativeQuery = true)
    List<Integer> findRelatedProductIds(@Param("type") String type, @Param("productId") long productId);
}
