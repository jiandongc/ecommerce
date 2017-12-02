package shoppingcart.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.Application;
import shoppingcart.domain.ShoppingCart;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

public class ShoppingCartRepositoryImplTest extends AbstractRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldCreateShoppingCartWithoutCustomerId() throws Exception {
        // Given & When
        final UUID uuid = shoppingCartRepository.create();

        // Then
        final ShoppingCart shoppingCart = shoppingCartRepository.findByUUID(uuid);
        assertThat(shoppingCart.getCartUid(), is(uuid));
    }

    @Test
    public void shouldCreateShoppingCartWithCustomerId() throws Exception {
        // Given & When
        final UUID uuid = shoppingCartRepository.create(1234L);

        // Then
        final ShoppingCart shoppingCart = shoppingCartRepository.findByUUID(uuid);
        assertThat(shoppingCart.getCartUid(), is(uuid));
        assertThat(shoppingCart.getCustomerId(), is(1234L));

    }

}