package shoppingcart.repository;

import shoppingcart.domain.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartItemRepository {
    void save(long cartId, ShoppingCartItem cartItem);
    List<ShoppingCartItem> findByCartId(long cartId);
}
