package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.data.CustomerData;
import shoppingcart.domain.*;
import shoppingcart.repository.ShoppingCartItemRepository;
import shoppingcart.repository.ShoppingCartRepository;
import shoppingcart.repository.VoucherRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ShoppingCartItemRepository cartItemRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    @Transactional
    public UUID createShoppingCartForGuest() {
        return cartRepository.create();
    }

    @Override
    @Transactional
    public UUID createShoppingCartForUser(CustomerData customerData) {
        return cartRepository.create(customerData.getId(), customerData.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> getShoppingCartByUid(UUID cartUid) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        cartOptional.ifPresent(cart -> cart.setShoppingCartItems(cartItemRepository.findByCartId(cart.getId())));
        cartOptional.ifPresent(cart -> cart.setShippingAddress(cartRepository.findAddress(cart.getId(), "Shipping").orElse(null)));
        cartOptional.ifPresent(cart -> cart.setBillingAddress(cartRepository.findAddress(cart.getId(), "Billing").orElse(null)));
        cartOptional.ifPresent(cart -> cart.setDeliveryOption(cartRepository.findDeliveryOption(cart.getId()).orElse(null)));
        cartOptional.ifPresent(cart -> cart.setPromotion(cartRepository.findPromotion(cart.getId()).orElse(null)));
        return cartOptional;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShoppingCart> getShoppingCartByCustomerUid(String customerUid) {
        final List<ShoppingCart> shoppingCarts = cartRepository.findByCustomerUid(UUID.fromString(customerUid));
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
            final List<ShoppingCart> shoppingCarts = cartRepository.findByCustomerUid(UUID.fromString(customerData.getId()));
            shoppingCarts.stream().filter(cart -> !cart.getCartUid().equals(cartUid)).forEach(this::deactivateShoppingCart);
            cartRepository.updateCustomerUid(cartUid, UUID.fromString(customerData.getId()));
        }
        cartRepository.updateEmail(cartUid, customerData.getEmail());
    }

    @Override
    @Transactional
    public void deactivateShoppingCart(ShoppingCart shoppingCart) {
        cartRepository.deactivateShoppingCart(shoppingCart.getId());
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

    @Override
    public void addPromotion(UUID cartUid, String voucherCode) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        final Optional<Voucher> voucherOptional = voucherRepository.findByVoucherCode(voucherCode);
        if (cartOptional.isPresent() && voucherOptional.isPresent()) {
            Promotion promotion = Promotion.builder()
                    .voucherCode(voucherOptional.get().getCode())
                    .voucherType(voucherOptional.get().getType())
                    .discountAmount(voucherOptional.get().getDiscountAmount())
                    .build();
            cartRepository.addPromotion(cartOptional.get().getId(), promotion);
        }
    }

    @Override
    public void deletePromotion(UUID cartUid) {
        final Optional<ShoppingCart> cartOptional = cartRepository.findByUUID(cartUid);
        cartOptional.ifPresent(cart -> cartRepository.deletePromotion(cart.getId()));
    }

    @Override
    public List<ShoppingCart> findShoppingCarts(Date date) {
        List<ShoppingCart> shoppingCarts = cartRepository.findAll();
        if (date != null) {
            return shoppingCarts.stream()
                    .filter(sc -> !sc.getCreationTime().before(date))
                    .sorted(Comparator.comparing(ShoppingCart::getCreationTime).reversed())
                    .collect(Collectors.toList());
        } else {
            return shoppingCarts.stream()
                    .sorted(Comparator.comparing(ShoppingCart::getCreationTime).reversed())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteShoppingCart(UUID cartUid) {
        Optional<ShoppingCart> shoppingCartOptional = this.getShoppingCartByUid(cartUid);
        shoppingCartOptional.ifPresent(cart -> cartRepository.delete(cart.getId()));
    }
}
