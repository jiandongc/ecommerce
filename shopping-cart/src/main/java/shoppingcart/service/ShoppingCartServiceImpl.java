package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.data.CustomerData;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<ShoppingCart> getShoppingCartByUid(UUID cartUid) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        cartOptional.ifPresent(cart -> cart.setShoppingCartItems(cartItemRepository.findByCartId(cart.getId())));
        cartOptional.ifPresent(cart -> cart.setShippingAddress(cartRepository.findAddress(cart.getId(), "Shipping").orElse(null)));
        cartOptional.ifPresent(cart -> cart.setBillingAddress(cartRepository.findAddress(cart.getId(), "Billing").orElse(null)));
        cartOptional.ifPresent(cart -> cart.setDeliveryOption(cartRepository.findDeliveryOption(cart.getId()).orElse(null)));
        return cartOptional;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> getShoppingCartByCustomerId(long customerId) {
        final List<ShoppingCart> shoppingCarts = cartRepository.findByCustomerId(customerId);
        if (shoppingCarts.size() > 0) {
            final ShoppingCart shoppingCart = shoppingCarts.get(0);
            shoppingCart.setShoppingCartItems(cartItemRepository.findByCartId(shoppingCart.getId()));
            shoppingCart.setShippingAddress(cartRepository.findAddress(shoppingCart.getId(), "Shipping").orElse(null));
            shoppingCart.setBillingAddress(cartRepository.findAddress(shoppingCart.getId(), "Billing").orElse(null));
            return Optional.of(shoppingCart);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void addCustomerInfo(UUID cartUid, CustomerData customerData) {
        if (customerData.getId() != null) {
            final List<ShoppingCart> shoppingCarts = cartRepository.findByCustomerId(customerData.getId());
            shoppingCarts.stream().filter(cart -> !cart.getCartUid().equals(cartUid))
                    .forEach(this::deleteShoppingCart);
            cartRepository.updateCustomerId(cartUid, customerData.getId());
        }
        cartRepository.updateEmail(cartUid, customerData.getEmail());
    }

    @Override
    @Transactional
    public void deleteShoppingCart(ShoppingCart shoppingCart) {
        cartItemRepository.deleteByCartId(shoppingCart.getId());
        cartRepository.delete(shoppingCart.getId());
    }

    @Override
    @Transactional
    public void addAddress(UUID cartUid, Address address) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        cartOptional.ifPresent(cart -> cartRepository.addAddress(cart.getId(), address));
    }

    @Override
    @Transactional
    public void addDeliveryOption(UUID cartUid, DeliveryOption deliveryOption) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        cartOptional.ifPresent(cart -> cartRepository.addDeliveryOption(cart.getId(), deliveryOption));
    }
}
