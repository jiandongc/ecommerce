package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Brand;

/**
 * Created by jiandong on 13/11/16.
 */
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
