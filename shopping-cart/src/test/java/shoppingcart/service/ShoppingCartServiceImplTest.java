package shoppingcart.service;

import org.junit.Test;
import shoppingcart.repository.ShoppingCartRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ShoppingCartServiceImplTest {

    private ShoppingCartRepository repository = mock(ShoppingCartRepository.class);
    private ShoppingCartService service = new ShoppingCartServiceImpl(repository);

    @Test
    public void shouldCreateShoppingCartForGuest(){
        // Given & When
        service.createShoppingCartForGuest();

        // Then
        verify(repository, times(1)).create();
    }

    @Test
    public void shouldCreateShoppingCartForUser(){
        // Given & When
        service.createShoppingCartForUser(1234L);

        // Then
        verify(repository, times(1)).create(1234L);
    }

}