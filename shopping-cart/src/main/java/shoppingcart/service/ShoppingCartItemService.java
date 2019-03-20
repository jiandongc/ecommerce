package shoppingcart.service;

import shoppingcart.domain.ShoppingCartItem;

import java.util.UUID;

public interface ShoppingCartItemService {
    void createCartItem(UUID uuid, ShoppingCartItem cartItem);
    void deleteCartItem(UUID uuid, String sku);
    void updateQuantity(UUID uuid, String sku, Integer quantity);
}
