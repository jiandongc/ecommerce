package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.Optional;
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
        final Optional<ShoppingCartItem> cartItemOptional = cartItemRepository.findByCartIdAndSku(cart.getId(), cartItem.getSku());
        if(cartItemOptional.isPresent()){
            cartItemRepository.updateQuantity(cart.getId(), cartItem, cartItemOptional.get().getQuantity() + 1);
        } else {
            cartItemRepository.save(cart.getId(), cartItem);
        }

        cart.setShoppingCartItems(cartItemRepository.findByCartId(cart.getId()));
        return cart;
    }

    @Override
    @Transactional
    public ShoppingCart deleteCartItem(UUID cartUid, String sku) {
        final ShoppingCart cart = cartRepository.findByUUID(cartUid).orElseThrow(() -> new RuntimeException(format("CartUId %s not found", cartUid)));
        cartItemRepository.delete(cart.getId(), sku);
        cart.setShoppingCartItems(cartItemRepository.findByCartId(cart.getId()));
        return cart;
    }
}
