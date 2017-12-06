package shoppingcart.repository;

import shoppingcart.domain.ShoppingCartItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartItemRepository {
    void save(long cartId, ShoppingCartItem cartItem);
    void updateQuantity(long cartId, ShoppingCartItem cartItem, int quantity);
    List<ShoppingCartItem> findByCartId(long cartId);
    Optional<ShoppingCartItem> findByCartIdAndSku(long cartId, String sku);
    void delete(long cartId, String sku);
}
