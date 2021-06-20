package shoppingcart.service;

import shoppingcart.domain.DeliveryOptionOffer;

import java.util.List;
import java.util.UUID;

public interface DeliveryOptionService {

    List<DeliveryOptionOffer> getDeliveryOptionOffers(UUID cartUid, String countryCode);

}
