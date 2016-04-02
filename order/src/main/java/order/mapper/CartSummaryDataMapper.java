package order.mapper;

import order.data.AnonCartItemData;
import order.data.AnonCartItemDataBuilder;
import order.data.CartSummaryData;
import order.data.CartSummaryDataBuilder;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by jiandong on 31/03/16.
 */

@Component
public class CartSummaryDataMapper {

    public CartSummaryData getValue(AnonCart anonCart) {

        final UUID cartUid = anonCart.getCartUid();

        final CartSummaryDataBuilder builder = CartSummaryDataBuilder.newBuilder()
                .setCartUid(cartUid)
                .setTotalPrice(anonCart.getTotalPrice())
                .setTotalCount(anonCart.getTotalCount());

        for (AnonCartItem anonCartItem : anonCart.getAnonCartItems()) {
            final AnonCartItemData anonCartItemData = AnonCartItemDataBuilder.newBuilder()
                    .setCartUid(cartUid)
                    .setProductId(anonCartItem.getProductId())
                    .setProductName(anonCartItem.getProductName())
                    .setProductPrice(anonCartItem.getProductPrice())
                    .setQuantity(anonCartItem.getQuantity())
                    .build();
            builder.addAnonCartItemData(anonCartItemData);
        }

        return builder.build();
    }
}
