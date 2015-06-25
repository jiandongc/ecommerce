package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;

public interface AnonCartService {
    public AnonCart addFirstItem(AnonCartItemData anonCartItemData);
    public AnonCart addAnotherItem(AnonCartItemData anonCartItemData);
}
