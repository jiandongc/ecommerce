package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository cartItemRepository;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartRepository cartRepository,
                                   ShoppingCartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public UUID createShoppingCartForGuest() {
        return cartRepository.create();
    }

    @Override
    @Transactional
    public UUID createShoppingCartForUser(long customerId) {
        return cartRepository.create(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCart getShoppingCartByUid(UUID cartUid) {
        final ShoppingCart shoppingCart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException(format("CartUId %s not found", cartUid)));
        shoppingCart.setShoppingCartItems(cartItemRepository.findByCartId(shoppingCart.getId()));
        return shoppingCart;
    }

    @Override
    @Transactional
    public Optional<ShoppingCart> updateCustomerId(UUID cartUid, Long customerId) {
        cartRepository.updateCustomerId(cartUid, customerId);
        return cartRepository.findByUUID(cartUid);
    }
}
