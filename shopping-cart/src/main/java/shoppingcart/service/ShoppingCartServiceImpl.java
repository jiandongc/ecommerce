package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ShoppingCartItemRepository cartItemRepository;

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
        final ShoppingCart shoppingCart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException(format("Shopping cart not found using CartUId [%s]", cartUid)));
        shoppingCart.setShoppingCartItems(cartItemRepository.findByCartId(shoppingCart.getId()));
        return shoppingCart;
    }

    @Override
    @Transactional
    public Optional<ShoppingCart> updateCustomerId(UUID cartUid, Long customerId) {
        final List<ShoppingCart> shoppingCarts = cartRepository.findByCustomerId(customerId);
        shoppingCarts.stream().filter(cart -> !cart.getCartUid().equals(cartUid))
                .forEach(this::deleteShoppingCart);
        cartRepository.updateCustomerId(cartUid, customerId);
        return cartRepository.findByUUID(cartUid);
    }

    @Override
    @Transactional
    public void deleteShoppingCart(ShoppingCart shoppingCart){
        cartItemRepository.deleteByCartId(shoppingCart.getId());
        cartRepository.delete(shoppingCart.getCartUid());
    }
}
