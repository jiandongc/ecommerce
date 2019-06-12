package shoppingcart.mapper;

import org.springframework.stereotype.Component;
import shoppingcart.data.AddressData;
import shoppingcart.data.CartData;
import shoppingcart.data.CartItemData;
import shoppingcart.data.DeliveryOptionData;
import shoppingcart.domain.Address;
import shoppingcart.domain.DeliveryOption;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.domain.ShoppingCartItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDataMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM. dd");

    public CartData map(ShoppingCart shoppingCart) {
        final Double subTotal = shoppingCart.getShoppingCartItems().stream().mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity()).sum();
        final int quantity = shoppingCart.getShoppingCartItems().stream().mapToInt(ShoppingCartItem::getQuantity).sum();
        final List<CartItemData> cartItems = shoppingCart.getShoppingCartItems().stream().map(cartItem ->
                CartItemData.builder()
                        .name(cartItem.getName())
                        .price(cartItem.getPrice())
                        .description(cartItem.getDescription())
                        .quantity(cartItem.getQuantity())
                        .sku(cartItem.getSku())
                        .thumbnail(cartItem.getImageUrl())
                        .code(cartItem.getCode())
                        .build()
        ).collect(Collectors.toList());
        final AddressData shipping = shoppingCart.getShippingAddress() != null ? map(shoppingCart.getShippingAddress()) : null;
        final AddressData billing = shoppingCart.getBillingAddress() != null ? map(shoppingCart.getBillingAddress()) : null;
        final DeliveryOptionData deliveryOptionData = shoppingCart.getDeliveryOption() != null ? map(shoppingCart.getDeliveryOption()) : null;

        return CartData.builder()
                .cartUid(shoppingCart.getCartUid().toString())
                .customerId(shoppingCart.getCustomerId())
                .quantity(quantity)
                .subTotal(subTotal)
                .cartItems(cartItems)
                .shipping(shipping)
                .billing(billing)
                .deliveryOption(deliveryOptionData)
                .build();
    }

    private AddressData map(Address address) {
        return AddressData.builder()
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

    private DeliveryOptionData map(DeliveryOption deliveryOption) {
        final String etaFromDate = formatDate(deliveryOption.getMinDaysRequired());
        final String etaToDate = formatDate(deliveryOption.getMaxDaysRequired());
        String eta;

        if (etaFromDate.equals(etaToDate)) {
            eta = etaFromDate;
        } else {
            eta = String.format("%s - %s", etaFromDate, etaToDate);
        }

        return DeliveryOptionData.builder()
                .method(deliveryOption.getMethod())
                .charge(deliveryOption.getCharge())
                .minDays(deliveryOption.getMinDaysRequired())
                .maxDays(deliveryOption.getMaxDaysRequired())
                .eta(eta)
                .build();
    }

    private String formatDate(int days) {
        LocalDate localDate = LocalDate.now().plusDays(days);
        String dayOfWeek = localDate.getDayOfWeek().name();
        dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
        return dayOfWeek + ", " + dateTimeFormatter.format(localDate);
    }
}
