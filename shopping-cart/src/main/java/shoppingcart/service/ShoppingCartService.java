package shoppingcart.service;

import shoppingcart.domain.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartService {

    UUID createShoppingCartForGuest();
    UUID createShoppingCartForUser(long customerId);
    ShoppingCart getShoppingCartByUid(UUID cartUid);
    Optional<ShoppingCart> updateCustomerId(UUID cartUid, Long customerId);
    void deleteShoppingCart(ShoppingCart shoppingCart);
}
