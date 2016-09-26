package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;

import java.util.Optional;
import java.util.UUID;

public interface AnonCartService {
    AnonCart addItem(AnonCartItemData anonCartItemData);
    Optional<AnonCart> findAnonCartByUid(UUID cartUid);
    Optional<AnonCart> findAnonCartByCustomerId(Long customerId);
    Optional<AnonCart> updateCartWithCustomerId(UUID cartUid, Long customerId);
    Optional<AnonCart> updateCartItemWithProductId(UUID cartUid, Long productId, AnonCartItemData anonCartItemData);
    void deleteCartItemByProductId(UUID cartUid, Long productId);

    // test only
    AnonCart findAnonCartByUidForTest(UUID cartUid);
}
