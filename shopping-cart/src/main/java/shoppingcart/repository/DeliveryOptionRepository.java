package shoppingcart.repository;

import shoppingcart.domain.DeliveryOptionOffer;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryOptionRepository {
    void addDeliveryOptionOffers(DeliveryOptionOffer deliveryOptionOffer);
    List<DeliveryOptionOffer> findDeliveryOptionOffersByCountryCode(String countryCode, LocalDate currentDate);
    List<DeliveryOptionOffer> findDefaultDeliveryOptionsByCountryCode(String countryCode);
}
