package order.repository;

import order.domain.AnonCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {

    Optional<AnonCart> findByCartUid(UUID uuid);

    void deleteByCustomerId(Long customerId);

    Optional<AnonCart> findByCustomerId(Long customerId);
}
