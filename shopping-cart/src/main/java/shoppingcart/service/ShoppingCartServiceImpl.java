package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.UUID;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    @Transactional
    public UUID createShoppingCartForGuest() {
        return shoppingCartRepository.create();
    }

    @Override
    @Transactional
    public UUID createShoppingCartForUser(long customerId) {
        return shoppingCartRepository.create(customerId);
    }
}
