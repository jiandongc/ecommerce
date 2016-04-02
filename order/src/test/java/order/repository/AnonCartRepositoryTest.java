package order.repository;

import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

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
    public void shouldFindAnonCartByCustomerId(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 1, 1);
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10);
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);
        anonCart.setCustomerId(12345l);
        anonCartRepository.save(anonCart);

        // When
        final AnonCart foundAnonCart = anonCartRepository.findByCustomerId(12345l);

        // Then
        assertThat(foundAnonCart, is(anonCart));
        assertThat(foundAnonCart.getTotalPrice(), is(11d));
    }

    @Test
    public void shouldDeleteCartByCustomerId(){
        // Given
        final Long customerId = 12345l;
        final Long anotherCustomerId = 54321l;

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

        final AnonCart anonCartThree = new AnonCart();
        final AnonCartItem itemThree = new AnonCartItem(3, "book3", 133, 13);
        anonCartThree.addAnonCartItem(itemThree);
        anonCartThree.setCustomerId(anotherCustomerId);
        anonCartRepository.save(anonCartThree);


        // When
        anonCartRepository.deleteByCustomerId(customerId);

        // Then
        final AnonCart actualCart = anonCartRepository.findByCartUid(anonCart.getCartUid());
        assertThat(actualCart, is(nullValue()));

        final AnonCart actualCartTwo = anonCartRepository.findByCartUid(anonCartTwo.getCartUid());
        assertThat(actualCartTwo, is(nullValue()));

        final AnonCart actualCartThree = anonCartRepository.findByCartUid(anonCartThree.getCartUid());
        assertThat(actualCartThree, is(anonCartThree));
    }
}
