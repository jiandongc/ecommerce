package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AnonCart addFirstItem(AnonCartItemData anonCartItemData) {
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(
                anonCartItemData.getProductId(),
                anonCartItemData.getProductName(),
                anonCartItemData.getProductPrice(),
                anonCartItemData.getQuantity(),
                anonCartItemData.getImageUrl());
        anonCart.addAnonCartItem(firstCartItem);
        return anonCartRepository.save(anonCart);
    }

    @Override
    @Transactional
    public AnonCart addAnotherItem(AnonCartItemData anonCartItemData) {
        final AnonCart anonCart = anonCartRepository.findByCartUid(anonCartItemData.getCartUid());
        if (anonCart != null) {
            for (AnonCartItem anonCartItem : anonCart.getAnonCartItems()) {
                if (anonCartItem.getProductId() == anonCartItemData.getProductId()) {
                    anonCartItem.setQuantity(anonCartItemData.getQuantity() + anonCartItem.getQuantity());
                    anonCartItem.setProductPrice(anonCartItemData.getProductPrice());
                    anonCartItem.setProductName(anonCartItemData.getProductName());
                    anonCartItem.setImageUrl(anonCartItemData.getImageUrl());
                    return anonCart;
                }
            }

            final AnonCartItem anotherCartItem = new AnonCartItem(
                    anonCartItemData.getProductId(),
                    anonCartItemData.getProductName(),
                    anonCartItemData.getProductPrice(),
                    anonCartItemData.getQuantity(),
                    anonCartItemData.getImageUrl());
            anonCart.addAnonCartItem(anotherCartItem);
        }
        return anonCart;
    }

    @Override
    @Transactional(readOnly = true)
    public AnonCart findAnonCartByUid(UUID cartUid) {
        return anonCartRepository.findByCartUid(cartUid);
    }

    @Override
    @Transactional(readOnly = true)
    public AnonCart findAnonCartByCustomerId(Long customerId) {
        return anonCartRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public AnonCart updateCartWithCustomerId(UUID cartUid, Long customerId) {
        anonCartRepository.deleteByCustomerId(customerId);
        final AnonCart anonCart = anonCartRepository.findByCartUid(cartUid);
        if (anonCart != null) {
            anonCart.setCustomerId(customerId);
        }
        return anonCart;
    }

    @Override
    @Transactional
    public void deleteCartItemByProductId(UUID cartUid, Long productId) {
        final AnonCart anonCart = anonCartRepository.findByCartUid(cartUid);
        if (anonCart != null) {
            for (AnonCartItem anonCartItem : anonCart.getAnonCartItems()) {
                if (anonCartItem.getProductId() == productId) {
                    anonCart.removeAnonCartItem(anonCartItem);
                    break;
                }
            }
        }
    }

    /**
     * This method should be used in test only. It would EAGER load the collection
     * @param cartUid
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public AnonCart findAnonCartByUidForTest(UUID cartUid) {
        final AnonCart anonCart = anonCartRepository.findByCartUid(cartUid);
        for (AnonCartItem anonCartItem : anonCart.getAnonCartItems()) {
            anonCartItem.getProductId();
        }
        return anonCart;
    }
}
