package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Vat;

public interface VatRepository extends JpaRepository<Vat, String> {
}
