package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;

import java.util.UUID;

public interface AnonCartService {
    AnonCart addFirstItem(AnonCartItemData anonCartItemData);
    AnonCart addAnotherItem(AnonCartItemData anonCartItemData);
    AnonCart findAnonCartByUid(UUID cartUid);
    AnonCart findAnonCartByCustomerId(Long customerId);
    AnonCart updateCartWithCustomerId(UUID cartUid, Long customerId);
    void deleteCartItemByProductId(UUID cartUid, Long productId);

    // test only
    AnonCart findAnonCartByUidForTest(UUID cartUid);
}
