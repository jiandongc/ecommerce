package order.repository;

import order.domain.AnonCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {

    AnonCart findByCartUid(UUID uuid);

    void deleteByCustomerId(Long customerId);

    AnonCart findByCustomerId(Long customerId);
}
