package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;

import java.util.UUID;

public interface AnonCartService {
    public AnonCart addFirstItem(AnonCartItemData anonCartItemData);
    public AnonCart addAnotherItem(AnonCartItemData anonCartItemData);
    public AnonCart findAnonCartByUid(UUID cartUid);
}
