package shoppingcart.mapper;

import org.springframework.stereotype.Component;
import shoppingcart.data.SimpleCartData;
import shoppingcart.domain.ShoppingCart;

@Component
public class SimpleCartDataMapper {

    public SimpleCartData map(ShoppingCart shoppingCart) {
        return SimpleCartData.builder()
                .cartUid(shoppingCart.getCartUid().toString())
                .email(shoppingCart.getEmail())
                .active(shoppingCart.isActive())
                .customerUid(shoppingCart.getCustomerUid() != null ? shoppingCart.getCustomerUid().toString() : null)
                .creationTime(shoppingCart.getCreationTime().toString())
                .build();
    }
}
