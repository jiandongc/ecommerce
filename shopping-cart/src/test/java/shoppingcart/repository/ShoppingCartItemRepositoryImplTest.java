package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

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

    @Test
    public void shouldUpdateCartItemQuantity(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(ONE)
                .sku("109283")
                .imageUrl("/image.jpeg")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);

        // When
        shoppingCartItemRepository.updateQuantity(cart.getId(), cartItem, 10);

        // Then
        final List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getName(), is("product"));
        assertThat(cartItems.get(0).getPrice(), is(ONE));
        assertThat(cartItems.get(0).getSku(), is("109283"));
        assertThat(cartItems.get(0).getQuantity(), is(10));
        assertThat(cartItems.get(0).getImageUrl(), is("/image.jpeg"));
    }

    @Test
    public void shouldFindCartItemByIdAndSku(){
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

        // Then
        final ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByCartIdAndSku(cart.getId(), "109283").orElseThrow(() -> new RuntimeException("item not found"));
        assertThat(shoppingCartItem.getName(), is("product"));
        assertThat(shoppingCartItem.getPrice(), is(ONE));
        assertThat(shoppingCartItem.getSku(), is("109283"));
        assertThat(shoppingCartItem.getQuantity(), is(1));
        assertThat(shoppingCartItem.getImageUrl(), is("/image.jpeg"));
    }

    @Test
    public void shouldReturnEmptyOptionalIfItemIsNotFound(){
        // Given & When
        Optional<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findByCartIdAndSku(1022L, "23443");

        // Then
        assertThat(shoppingCartItem.isPresent(), is(false));
    }

}