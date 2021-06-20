package shoppingcart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingcart.domain.DeliveryOptionOffer;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.repository.DeliveryOptionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliveryOptionServiceImpl implements DeliveryOptionService {

    private final DeliveryOptionRepository deliveryOptionRepository;

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public DeliveryOptionServiceImpl(DeliveryOptionRepository deliveryOptionRepository, ShoppingCartService shoppingCartService) {
        this.deliveryOptionRepository = deliveryOptionRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryOptionOffer> getDeliveryOptionOffers(UUID cartUid, String countryCode) {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.getShoppingCartByUid(cartUid);
        BigDecimal itemSubTotal = shoppingCart.map(ShoppingCart::getItemSubTotal).orElse(BigDecimal.ZERO);
        List<DeliveryOptionOffer> deliveryOptionOffers = getDeliveryOptionOffers(countryCode);
        return deliveryOptionOffers.stream()
                .filter(deliveryOptionOffer -> BigDecimal.valueOf(deliveryOptionOffer.getMinSpend()).compareTo(itemSubTotal) <= 0)
                .collect(Collectors.toList());
    }

    private List<DeliveryOptionOffer> getDeliveryOptionOffers(String countryCode) {
        List<DeliveryOptionOffer> deliveryOptionOffers = deliveryOptionRepository.findDeliveryOptionOffersByCountryCode(countryCode, LocalDate.now());
        if (deliveryOptionOffers.isEmpty()) {
            deliveryOptionOffers = deliveryOptionRepository.findDefaultDeliveryOptionsByCountryCode(countryCode);
        }
        return deliveryOptionOffers;
    }

}
