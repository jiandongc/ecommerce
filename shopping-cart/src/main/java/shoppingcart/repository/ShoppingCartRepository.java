package shoppingcart.repository;

import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.Promotion;
import shoppingcart.domain.ShoppingCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository {
    UUID create();
    UUID create(String customerUid, String email);
    List<ShoppingCart> findAll();
    Optional<ShoppingCart> findByUUID(UUID uuid);
    List<ShoppingCart> findByCustomerUid(UUID customerUid);
    int updateCustomerUid(UUID cartUid, UUID customerUid);
    int updateEmail(UUID cartUid, String email);
    int delete(long cartId);
    void deactivateShoppingCart(long cartId);
    void addAddress(long cartId, Address address);
    Optional<Address> findAddress(long cartId, String addressType);
    void addDeliveryOption(long cartId, DeliveryOption deliveryOption);
    Optional<DeliveryOption> findDeliveryOption(long cartId);
    void addPromotion(long cartId, Promotion promotion);
    Optional<Promotion> findPromotion(long cartId);
    void deletePromotion(long cartId);
}
