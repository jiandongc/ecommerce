package shoppingcart.service;

import java.util.UUID;

public interface ShoppingCartService {

    UUID createShoppingCartForGuest();
    UUID createShoppingCartForUser(long customerId);

}
