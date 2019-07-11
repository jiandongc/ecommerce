package shoppingcart.mapper;

import org.springframework.stereotype.Component;
import shoppingcart.data.AddressData;
import shoppingcart.data.ItemData;
import shoppingcart.data.OrderData;
import shoppingcart.domain.Address;
import shoppingcart.domain.ShoppingCart;

import java.math.BigDecimal;

@Component
public class OrderDataMapper {

    public OrderData map(ShoppingCart shoppingCart) {

        BigDecimal itemSubTotal = shoppingCart.getItemSubTotal();
        BigDecimal itemsVat = shoppingCart.getItemsVat();
        BigDecimal itemsBeforeVat = itemSubTotal.subtract(itemsVat);

        BigDecimal postage = shoppingCart.getPostage();
        BigDecimal postageVat = shoppingCart.getPostageVat();
        BigDecimal postageBeforeVat = postage.subtract(postageVat);

        BigDecimal promotion = BigDecimal.ZERO;
        BigDecimal promotionVat = BigDecimal.ZERO;
        BigDecimal promotionBeforeVat = BigDecimal.ZERO;

        BigDecimal totalBeforeVat = itemsBeforeVat.add(postageBeforeVat).add(promotionBeforeVat);
        BigDecimal totalVat = itemsVat.add(postageVat).add(promotionVat);
        BigDecimal orderTotal = shoppingCart.getOrderTotal();

        OrderData orderData = OrderData.builder()
                .customerId(shoppingCart.getCustomerId())
                .items(itemsBeforeVat)
                .postage(postageBeforeVat)
                .promotion(promotionBeforeVat)
                .totalBeforeVat(totalBeforeVat)
                .itemsVat(itemsVat)
                .postageVat(postageVat)
                .promotionVat(promotionVat)
                .totalVat(totalVat)
                .orderTotal(orderTotal)
                .deliveryMethod(shoppingCart.getDeliveryOption().getMethod())
                .minDaysRequired(shoppingCart.getDeliveryOption().getMinDaysRequired())
                .maxDaysRequired(shoppingCart.getDeliveryOption().getMaxDaysRequired())
                .build();

        shoppingCart.getShoppingCartItems().forEach(cartItem -> {
            BigDecimal subTotal = BigDecimal.valueOf(cartItem.getPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            ItemData itemData = ItemData.builder()
                    .sku(cartItem.getSku())
                    .code(cartItem.getCode())
                    .name(cartItem.getName())
                    .description(cartItem.getDescription())
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .subTotal(subTotal)
                    .imageUrl(cartItem.getImageUrl())
                    .build();
            orderData.addOrderItem(itemData);
        });

        if (shoppingCart.getShippingAddress() != null) {
            orderData.addOrderAddresses(map(shoppingCart.getShippingAddress()));
        }

        if (shoppingCart.getBillingAddress() != null) {
            orderData.addOrderAddresses(map(shoppingCart.getBillingAddress()));
        }

        return orderData;
    }

    private AddressData map(Address address) {
        return AddressData.builder()
                .addressType(address.getAddressType())
                .name(address.getName())
                .title(address.getTitle())
                .mobile(address.getMobile())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .addressLine3(address.getAddressLine3())
                .city(address.getCity())
                .country(address.getCountry())
                .postcode(address.getPostcode())
                .build();
    }
}
