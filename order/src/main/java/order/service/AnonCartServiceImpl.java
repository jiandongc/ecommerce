package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnonCartServiceImpl implements AnonCartService {

    private AnonCartRepository anonCartRepository;

    @Autowired
    public AnonCartServiceImpl(AnonCartRepository anonCartRepository) {
        this.anonCartRepository = anonCartRepository;
    }

    @Override
    @Transactional
    public AnonCart addItem(AnonCartItemData anonCartItemData) {
        final Optional<AnonCart> anonCart = anonCartRepository.findByCartUid(anonCartItemData.getCartUid());
        if (anonCart.isPresent()) {

            final Optional<AnonCartItem> existingItem = anonCart.get().getAnonCartItems().stream()
                    .filter(item -> item.getProductId() == anonCartItemData.getProductId()).findFirst();

            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(anonCartItemData.getQuantity() + existingItem.get().getQuantity());
                existingItem.get().setProductPrice(anonCartItemData.getProductPrice());
                existingItem.get().setProductName(anonCartItemData.getProductName());
                existingItem.get().setImageUrl(anonCartItemData.getImageUrl());
            } else {
                final AnonCartItem newItem = new AnonCartItem(anonCartItemData.getProductId(), anonCartItemData.getProductName(),
                        anonCartItemData.getProductPrice(), anonCartItemData.getQuantity(), anonCartItemData.getImageUrl());
                anonCart.get().addAnonCartItem(newItem);
            }

            return anonCart.get();

        } else {
            final AnonCart newCart = new AnonCart();
            newCart.addAnonCartItem(new AnonCartItem(
                    anonCartItemData.getProductId(),
                    anonCartItemData.getProductName(),
                    anonCartItemData.getProductPrice(),
                    anonCartItemData.getQuantity(),
                    anonCartItemData.getImageUrl()));
            return anonCartRepository.save(newCart);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnonCart> findAnonCartByUid(UUID cartUid) {
        return anonCartRepository.findByCartUid(cartUid);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnonCart> findAnonCartByCustomerId(Long customerId) {
        return anonCartRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Optional<AnonCart> updateCartWithCustomerId(UUID cartUid, Long customerId) {
        anonCartRepository.deleteByCustomerId(customerId);
        final Optional<AnonCart> anonCart = anonCartRepository.findByCartUid(cartUid);
        anonCart.ifPresent(cart -> cart.setCustomerId(customerId));
        return anonCart;
    }

    @Override
    @Transactional
    public Optional<AnonCart> updateCartItemWithProductId(UUID cartUid, Long productId, AnonCartItemData anonCartItemData) {
        final Optional<AnonCart> cart = anonCartRepository.findByCartUid(cartUid);
        cart.ifPresent(v -> v.getAnonCartItems().stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst().ifPresent(item -> item.setQuantity(anonCartItemData.getQuantity())));
        return cart;
    }

    @Override
    @Transactional
    public void deleteCartItemByProductId(UUID cartUid, Long productId) {
        final Optional<AnonCart> cart = anonCartRepository.findByCartUid(cartUid);
        cart.ifPresent(v -> v.getAnonCartItems().stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst().ifPresent(item -> cart.get().removeAnonCartItem(item)));
    }

    /**
     * This method should be used in test only. It would EAGER load the collection
     *
     * @param cartUid
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public AnonCart findAnonCartByUidForTest(UUID cartUid) {
        final Optional<AnonCart> anonCart = anonCartRepository.findByCartUid(cartUid);
        anonCart.ifPresent(cart -> cart.getAnonCartItems().stream().forEach(AnonCartItem::getProductId));
        return anonCart.orElseThrow(NoSuchElementException::new);
    }
}
