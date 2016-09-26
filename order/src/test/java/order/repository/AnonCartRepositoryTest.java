package order.repository;

import order.domain.AnonCart;
import order.domain.AnonCartItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnonCartRepositoryTest extends AbstractRepositoryTest{

    @Autowired
    private AnonCartRepository anonCartRepository;

    @Test
    public void shouldSaveAnonCartWithMultipleItems(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10, "http://pen.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);

        // When
        anonCartRepository.save(anonCart);

        // Then
        AnonCart actualAnonCart = anonCartRepository.findOne(anonCart.getId());
        assertThat(actualAnonCart, is(anonCart));
    }

    @Test
    public void shouldFindAnonCartByCartUid(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10, "http://pen.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);
        anonCartRepository.save(anonCart);

        // When
        final UUID cartUid = anonCart.getCartUid();
        final Optional<AnonCart> actualAnonCart = anonCartRepository.findByCartUid(cartUid);

        // Then
        assertThat(actualAnonCart.isPresent(), is(true));
        assertThat(actualAnonCart.get(), is(anonCart));
        assertThat(actualAnonCart.get().getTotalPrice(), is(22d));
    }

    @Test
    public void shouldFindAnonCartByCustomerId(){
        // Given
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 1, 1, "http://book.jpeg");
        final AnonCartItem itemTwo = new AnonCartItem(2, "pen", 1, 10, "http://pen.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.addAnonCartItem(itemTwo);
        anonCart.setCustomerId(12345l);
        anonCartRepository.save(anonCart);

        // When
        final Optional<AnonCart> actualAnonCart = anonCartRepository.findByCustomerId(12345l);

        // Then
        assertThat(actualAnonCart.isPresent(), is(true));
        assertThat(actualAnonCart.get(), is(anonCart));
        assertThat(actualAnonCart.get().getTotalPrice(), is(11d));
    }

    @Test
    public void shouldDeleteCartByCustomerId(){
        // Given
        final Long customerId = 12345l;
        final Long anotherCustomerId = 54321l;

        final AnonCart anonCart = new AnonCart();
        final AnonCartItem itemOne = new AnonCartItem(1, "book", 12, 1, "http://book.jpeg");
        anonCart.addAnonCartItem(itemOne);
        anonCart.setCustomerId(customerId);
        anonCartRepository.save(anonCart);

        final AnonCart anonCartTwo = new AnonCart();
        final AnonCartItem itemTwo = new AnonCartItem(2, "book2", 122, 12, "http://book.jpeg");
        anonCartTwo.addAnonCartItem(itemTwo);
        anonCartTwo.setCustomerId(customerId);
        anonCartRepository.save(anonCartTwo);

        final AnonCart anonCartThree = new AnonCart();
        final AnonCartItem itemThree = new AnonCartItem(3, "book3", 133, 13, "http://book.jpeg");
        anonCartThree.addAnonCartItem(itemThree);
        anonCartThree.setCustomerId(anotherCustomerId);
        anonCartRepository.save(anonCartThree);


        // When
        anonCartRepository.deleteByCustomerId(customerId);

        // Then
        final Optional<AnonCart> actualCart = anonCartRepository.findByCartUid(anonCart.getCartUid());
        assertThat(actualCart.isPresent(), is(false));

        final Optional<AnonCart> actualCartTwo = anonCartRepository.findByCartUid(anonCartTwo.getCartUid());
        assertThat(actualCartTwo.isPresent(), is(false));

        final Optional<AnonCart> actualCartThree = anonCartRepository.findByCartUid(anonCartThree.getCartUid());
        assertThat(actualCartThree.isPresent(), is(true));
        assertThat(actualCartThree.get(), is(anonCartThree));
    }
}
