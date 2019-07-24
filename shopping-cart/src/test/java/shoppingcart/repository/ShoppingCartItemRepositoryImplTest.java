package shoppingcart.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ROUND_HALF_UP;
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
                .code("code1")
                .price(BigDecimal.valueOf(1.0))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);

        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .code("code2")
                .price(BigDecimal.valueOf(10.0))
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);

        // Then
        final List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(2));
        assertThat(cartItems.get(0).getName(), is("product"));
        assertThat(cartItems.get(0).getCode(), is("code1"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(cartItems.get(0).getSku(), is("109283"));
        assertThat(cartItems.get(0).getQuantity(), is(1));
        assertThat(cartItems.get(0).getImageUrl(), is("/image.jpeg"));
        assertThat(cartItems.get(0).getDescription(), is("Size: S"));
        assertThat(cartItems.get(1).getName(), is("product2"));
        assertThat(cartItems.get(1).getCode(), is("code2"));
        assertThat(cartItems.get(1).getPrice(), is(BigDecimal.valueOf(10).setScale(2, ROUND_HALF_UP)));
        assertThat(cartItems.get(1).getSku(), is("219283"));
        assertThat(cartItems.get(1).getQuantity(), is(1));
        assertThat(cartItems.get(1).getImageUrl(), is("/image2.jpeg"));
        assertThat(cartItems.get(1).getDescription(), is("Size: M"));
    }

    @Test
    public void shouldUpdateCartItemQuantity(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(BigDecimal.valueOf(1.0))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);

        // When
        shoppingCartItemRepository.updateQuantity(cart.getId(), "109283", 10);

        // Then
        final List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getName(), is("product"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(cartItems.get(0).getSku(), is("109283"));
        assertThat(cartItems.get(0).getQuantity(), is(10));
        assertThat(cartItems.get(0).getImageUrl(), is("/image.jpeg"));
        assertThat(cartItems.get(0).getDescription(), is("Size: S"));
    }

    @Test
    public void shouldFindCartItemByIdAndSku(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));

        // When
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(BigDecimal.valueOf(1))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);

        // Then
        final ShoppingCartItem shoppingCartItem = shoppingCartItemRepository.findByCartIdAndSku(cart.getId(), "109283").orElseThrow(() -> new RuntimeException("item not found"));
        assertThat(shoppingCartItem.getName(), is("product"));
        assertThat(shoppingCartItem.getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(shoppingCartItem.getSku(), is("109283"));
        assertThat(shoppingCartItem.getQuantity(), is(1));
        assertThat(shoppingCartItem.getImageUrl(), is("/image.jpeg"));
        assertThat(shoppingCartItem.getDescription(), is("Size: S"));
    }

    @Test
    public void shouldReturnEmptyOptionalIfItemIsNotFound(){
        // Given & When
        Optional<ShoppingCartItem> shoppingCartItem = shoppingCartItemRepository.findByCartIdAndSku(1022L, "23443");

        // Then
        assertThat(shoppingCartItem.isPresent(), is(false));
    }

    @Test
    public void shouldDeleteAnItemFromAShoppingCart(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(BigDecimal.valueOf(1.0))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);
        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .price(BigDecimal.valueOf(10.0))
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);

        // When & Then
        shoppingCartItemRepository.delete(cart.getId(), "219283");
        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(1));
        assertThat(cartItems.get(0).getName(), is("product"));
        assertThat(cartItems.get(0).getPrice(), is(BigDecimal.valueOf(1).setScale(2, ROUND_HALF_UP)));
        assertThat(cartItems.get(0).getSku(), is("109283"));
        assertThat(cartItems.get(0).getQuantity(), is(1));
        assertThat(cartItems.get(0).getImageUrl(), is("/image.jpeg"));
        assertThat(cartItems.get(0).getDescription(), is("Size: S"));

        // When & Then
        shoppingCartItemRepository.delete(cart.getId(), "109283");
        cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(0));
    }

    @Test
    public void shouldDeleteAllItemsForGivenCartId(){
        // Given
        final UUID uuid = shoppingCartRepository.create();
        final ShoppingCart cart = shoppingCartRepository.findByUUID(uuid).orElseThrow(() -> new RuntimeException("cart uid not found"));
        final ShoppingCartItem cartItem = ShoppingCartItem.builder()
                .name("product")
                .price(BigDecimal.valueOf(1.0))
                .sku("109283")
                .imageUrl("/image.jpeg")
                .description("Size: S")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem);
        final ShoppingCartItem cartItem2 = ShoppingCartItem.builder()
                .name("product2")
                .price(BigDecimal.valueOf(10.0))
                .sku("219283")
                .imageUrl("/image2.jpeg")
                .description("Size: M")
                .build();
        shoppingCartItemRepository.save(cart.getId(), cartItem2);

        List<ShoppingCartItem> cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(2));

        // When
        shoppingCartItemRepository.deleteByCartId(cart.getId());

        // Then
        cartItems = shoppingCartItemRepository.findByCartId(cart.getId());
        assertThat(cartItems.size(), is(0));
    }

}