package order.service;

import order.data.AnonCartItemData;
import order.domain.AnonCart;
import order.domain.AnonCartItem;
import order.repository.AnonCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AnonCartServiceImpl implements AnonCartService {

    private AnonCartRepository anonCartRepository;

    @Autowired
    public AnonCartServiceImpl(AnonCartRepository anonCartRepository){
        this.anonCartRepository = anonCartRepository;
    }

    @Override
    @Transactional
    public AnonCart addFirstItem(AnonCartItemData anonCartItemData) {
        final AnonCart anonCart = new AnonCart();
        final AnonCartItem firstCartItem = new AnonCartItem(anonCartItemData.getProductId(),
                anonCartItemData.getProductName(), anonCartItemData.getProductPrice(), anonCartItemData.getQuantity());
        anonCart.addAnonCartItem(firstCartItem);
        return anonCartRepository.save(anonCart);
    }

    @Override
    @Transactional
    public AnonCart addAnotherItem(AnonCartItemData anonCartItemData) {
        AnonCart anonCart = anonCartRepository.findByCartUid(anonCartItemData.getCartUid());
        final AnonCartItem anotherCartItem = new AnonCartItem(anonCartItemData.getProductId(),
                anonCartItemData.getProductName(), anonCartItemData.getProductPrice(), anonCartItemData.getQuantity());
        anonCart.addAnonCartItem(anotherCartItem);
        return anonCartRepository.save(anonCart);
    }

    @Override
    @Transactional
    public AnonCart findAnonCartByUid(UUID cartUid) {
        return anonCartRepository.findByCartUid(cartUid);
    }

    @Override
    @Transactional
    public AnonCart findAnonCartByCustomerId(Long customerId) {
        return anonCartRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public AnonCart updateCustomerId(UUID cartUid, Long customerId) {
        anonCartRepository.deleteByCustomerId(customerId);
        final AnonCart anonCart = anonCartRepository.findByCartUid(cartUid);
        if(anonCart != null){
            anonCart.setCustomerId(customerId);
        }
        return anonCart;
    }
}
