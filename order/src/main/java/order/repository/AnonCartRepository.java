package order.repository;

import order.domain.AnonCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {
    AnonCart findByCartUid(UUID uuid);
}
