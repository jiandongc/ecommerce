package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.ProductTag;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {
}
