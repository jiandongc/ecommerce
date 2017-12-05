package shoppingcart.mapper;

import org.junit.Test;
import shoppingcart.data.CartSummary;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CartSummaryMapperTest {

    CartSummaryMapper mapper = new CartSummaryMapper();

    @Test
    public void shouldMapShoppingCartToCartSummary(){
        // Given

        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(ONE)
                .sku("109283")
                .quantity(1)
                .imageUrl("/image.jpeg")
                .build();

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .price(TEN)
                .sku("219283")
                .quantity(10)
                .imageUrl("/image2.jpeg")
                .build();

        final UUID cartUid = UUID.randomUUID();
        final ShoppingCart shoppingCart = ShoppingCart.builder().cartUid(cartUid).build();
        shoppingCart.setShoppingCartItems(Arrays.asList(cartItem, cartItem2));

        // When
        final CartSummary cartSummary = mapper.map(shoppingCart);

        // Then
        assertThat(cartSummary.getItemsSubTotal(), is(BigDecimal.valueOf(101L)));
        assertThat(cartSummary.getTotalQuantity(), is(11));
        assertThat(cartSummary.getShoppingCart().getCartUid(), is(cartUid));

    }

}