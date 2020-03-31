package shoppingcart.service;

import shoppingcart.data.CustomerData;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartService {

    UUID createShoppingCartForGuest();
    UUID createShoppingCartForUser(CustomerData customerData);
    Optional<ShoppingCart> getShoppingCartByUid(UUID cartUid);
    Optional<ShoppingCart> getShoppingCartByCustomerId(long customerId);
    void addCustomerInfo(UUID cartUid, CustomerData customerData);
    void deleteShoppingCart(ShoppingCart shoppingCart);
    void addAddress(UUID cartUid, Address address);
    void addDeliveryOption(UUID cartUid, DeliveryOption deliveryOption);
}
