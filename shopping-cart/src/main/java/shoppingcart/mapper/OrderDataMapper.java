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

        BigDecimal itemsVat = shoppingCart.getItemsVat();
        BigDecimal itemsBeforeVat = shoppingCart.getItemsBeforeVat();

        BigDecimal postageVat = shoppingCart.getPostageVat();
        BigDecimal postageBeforeVat = shoppingCart.getPostageBeforeVat();

        BigDecimal discountVat = shoppingCart.getDiscountVat();
        BigDecimal discountBeforeVat = shoppingCart.getDiscountBeforeVat();

        BigDecimal totalBeforeVat = shoppingCart.getTotalBeforeVat();
        BigDecimal totalVat = shoppingCart.getVatTotal();
        BigDecimal orderTotal = shoppingCart.getOrderTotal();

        OrderData orderData = OrderData.builder()
                .customerId(shoppingCart.getCustomerUid() != null ? shoppingCart.getCustomerUid().toString() : null)
                .email(shoppingCart.getEmail())
                .items(itemsBeforeVat)
                .postage(postageBeforeVat)
                .discount(discountBeforeVat)
                .totalBeforeVat(totalBeforeVat)
                .itemsVat(itemsVat)
                .postageVat(postageVat)
                .discountVat(discountVat)
                .totalVat(totalVat)
                .orderTotal(orderTotal)
                .deliveryMethod(shoppingCart.getDeliveryOption().getMethod())
                .minDaysRequired(shoppingCart.getDeliveryOption().getMinDaysRequired())
                .maxDaysRequired(shoppingCart.getDeliveryOption().getMaxDaysRequired())
                .build();

        shoppingCart.getShoppingCartItems().forEach(cartItem -> {
            ItemData itemData = ItemData.builder()
                    .sku(cartItem.getSku())
                    .code(cartItem.getCode())
                    .name(cartItem.getName())
                    .description(cartItem.getDescription())
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .subTotal(cartItem.getGrossTotal())
                    .imageUrl(cartItem.getImageUrl())
                    .vatRate(cartItem.getVatRate())
                    .vat(cartItem.getVat())
                    .sale(cartItem.getNetAmount())
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
