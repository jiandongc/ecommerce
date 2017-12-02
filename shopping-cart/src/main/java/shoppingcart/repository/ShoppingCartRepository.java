package shoppingcart.repository;

import shoppingcart.domain.ShoppingCart;

import java.util.UUID;

public interface ShoppingCartRepository {

    UUID create();
    UUID create(long customerId);
    ShoppingCart findByUUID(UUID uuid);
}
