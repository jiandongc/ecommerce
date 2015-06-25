package order.repository;

import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.util.collections.Sets.newSet;

public class AnonCartRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private AnonCartRepository anonCartRepository;

    @Test
    public void shouldSaveAnonCartWithMultipleItems(){
        // Given
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        final AnonCart anonCart = new AnonCart(newSet(itemOne, itemTwo));

        // When
        anonCartRepository.save(anonCart);

        // Then
        AnonCart savedAnonCart = anonCartRepository.findOne(anonCart.getId());
        assertThat(savedAnonCart, is(anonCart));
    }

    @Test
    public void shouldFindAnonCartByCartUid(){
        // Given
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        final AnonCart anonCart = new AnonCart(newSet(itemOne, itemTwo));
        anonCartRepository.save(anonCart);

        // When
        final UUID cartUid = anonCart.getCartUid();
        final AnonCart foundAnonCart = anonCartRepository.findByCartUid(cartUid);

        // Then
        assertThat(foundAnonCart, is(anonCart));
    }
}
