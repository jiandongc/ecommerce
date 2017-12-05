package shoppingcart.mapper;

import org.springframework.stereotype.Component;
import shoppingcart.data.CartSummary;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.math.BigDecimal;

@Component
public class CartSummaryMapper {

    public CartSummary map(ShoppingCart shoppingCart) {
        final BigDecimal itemsSubTotal = shoppingCart.getShoppingCartItems().stream()
                .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        final int totalQuantity = shoppingCart.getShoppingCartItems().stream().mapToInt(ShoppingCartItem::getQuantity).sum();
        return CartSummary.builder()
                .itemsSubTotal(itemsSubTotal)
                .totalQuantity(totalQuantity)
                .shoppingCart(shoppingCart)
                .build();
    }

}
