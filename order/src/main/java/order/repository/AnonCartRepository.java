package order.repository;

import order.domain.AnonCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {

    AnonCart findByCartUid(UUID uuid);

    @Modifying
    @Query("delete from AnonCart ac where ac.cartUid <> :cartId and ac.customerId = :customerId")
    void deleteOtherCartsForSameCustomer(@Param("cartId") UUID cartId, @Param("customerId") Long customerId);
}
