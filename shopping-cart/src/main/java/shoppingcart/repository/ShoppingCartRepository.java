package shoppingcart.repository;

import shoppingcart.domain.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository {
    UUID create();
    UUID create(long customerId);
    Optional<ShoppingCart> findByUUID(UUID uuid);
}
