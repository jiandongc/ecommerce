package shoppingcart.service;

import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.util.UUID;

public interface ShoppingCartItemService {
    ShoppingCart createCartItem(UUID uuid, ShoppingCartItem cartItem);
    ShoppingCart deleteCartItem(UUID uuid, String sku);
}
