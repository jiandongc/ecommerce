package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ShoppingCartItemRepositoryImplTest extends AbstractRepositoryTest {

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldSaveShoppingCartItem(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));

        // When
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(ONE)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .price(TEN)
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);

        // Then
        final List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(2));
        assertThat(cartItems.get(0).getName(), is("product"));
        assertThat(cartItems.get(0).getPrice(), is(ONE));
        assertThat(cartItems.get(0).getSku(), is("109283"));
        assertThat(cartItems.get(0).getQuantity(), is(1));
        assertThat(cartItems.get(0).getImageUrl(), is("/image.jpeg"));
        assertThat(cartItems.get(1).getName(), is("product2"));
        assertThat(cartItems.get(1).getPrice(), is(TEN));
        assertThat(cartItems.get(1).getSku(), is("219283"));
        assertThat(cartItems.get(1).getQuantity(), is(1));
        assertThat(cartItems.get(1).getImageUrl(), is("/image2.jpeg"));
    }

}