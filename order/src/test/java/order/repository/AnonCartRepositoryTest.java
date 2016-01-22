package order.repository;

import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.internal.util.collections.Sets.newSet;

public class AnonCartRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private AnonCartRepository anonCartRepository;

    @Test
    public void shouldSaveAnonCartWithMultipleItems(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);

        // When
        anonCartRepository.save(anonCart);

        // Then
        AnonCart savedAnonCart = anonCartRepository.findOne(anonCart.getId());
        assertThat(savedAnonCart, is(anonCart));
    }

    @Test
    public void shouldFindAnonCartByCartUid(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);
        anonCartRepository.save(anonCart);

        // When
        final UUID cartUid = anonCart.getCartUid();
        final AnonCart foundAnonCart = anonCartRepository.findByCartUid(cartUid);

        // Then
        assertThat(foundAnonCart, is(anonCart));
        assertThat(foundAnonCart.getTotalPrice(), is(22d));
    }

    @Test
    public void shouldDeleteCartByCustomerId(){
        // Given
        final Long customerId = 12345l;

        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1);
        anonCart.addAnonCartItem(itemOne);
        anonCart.setCustomerId(customerId);
        anonCartRepository.save(anonCart);

        final AnonCart anonCartTwo = new AnonCart();
        final AnonCartItem itemTwo = new AnonCartItem(2, "book2", 122, 12);
        anonCartTwo.addAnonCartItem(itemTwo);
        anonCartTwo.setCustomerId(customerId);
        anonCartRepository.save(anonCartTwo);

        // When
        anonCartRepository.deleteOtherCartsForSameCustomer(anonCart.getCartUid(), customerId);

        // Then
        final AnonCart actualCart = anonCartRepository.findByCartUid(anonCart.getCartUid());
        assertThat(actualCart, is(anonCart));

        final AnonCart actualCartTwo = anonCartRepository.findByCartUid(anonCartTwo.getCartUid());
        assertThat(actualCartTwo, is(nullValue()));
    }
}
