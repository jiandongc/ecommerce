package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Optional<Brand> findByCode(String code);
}
