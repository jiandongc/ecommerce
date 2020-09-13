package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Vat;

import java.util.Optional;

public interface VatRepository extends JpaRepository<Vat, String> {

    Optional<Vat> findByName(String name);
}
