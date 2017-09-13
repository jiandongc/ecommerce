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
}
