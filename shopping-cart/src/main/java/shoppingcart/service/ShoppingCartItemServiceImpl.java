package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {

    private final ShoppingCartRepository cartRepository;
    private final ShoppingCartItemRepository cartItemRepository;

    @Autowired
    public ShoppingCartItemServiceImpl(ShoppingCartRepository cartRepository,
                                       ShoppingCartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public ShoppingCart createCartItem(UUID cartUid, ShoppingCartItem cartItem) {
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException(format("CartUId %s not found", cartUid)));
        cartItemRepository.save(cart.getId(), cartItem);
        cart.setShoppingCartItems(cartItemRepository.findByCartId(cart.getId()));
        return cart;
    }
}
