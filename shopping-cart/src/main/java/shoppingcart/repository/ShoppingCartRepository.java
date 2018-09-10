package shoppingcart.repository;

import shoppingcart.domain.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository {
    UUID create();
    UUID create(long customerId);
    Optional<ShoppingCart> findByUUID(UUID uuid);
    List<ShoppingCart> findByCustomerId(Long customerId);
    int updateCustomerId(UUID cartUid, Long customerId);
    int delete(UUID cartUid);
}
