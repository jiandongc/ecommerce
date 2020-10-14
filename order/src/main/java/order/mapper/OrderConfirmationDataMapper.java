package order.mapper;

import email.data.OrderConfirmationData;
import order.domain.Order;
import order.domain.OrderAddress;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConfirmationDataMapper {

    public OrderConfirmationData map(Order order,
                                     String name,
                                     String siteName,
                                     String registrationPage,
                                     String homePage) {
        OrderAddress shippingAddress = order.getShippingAddress().get();
        List<OrderConfirmationData.OrderItemData> orderItems = order.getOrderItems().stream()
                .map(item -> OrderConfirmationData.OrderItemData.builder()
                        .code(item.getCode())
                        .description(item.getDescription())
                        .imageUrl(item.getImageUrl())
                        .name(item.getName())
                        .quantity(String.valueOf(item.getQuantity()))
                        .price(item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString())
                        .subTotal(item.getSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString())
                        .sku(item.getSku())
                        .build())
                .collect(Collectors.toList());
        return OrderConfirmationData.builder()
                .sendTo(Arrays.asList(order.getEmail()))
                .orderNumber(order.getOrderNumber())
                .orderEta(order.getEta())
                .orderDeliveryMethod(order.getDeliveryMethod())
                .orderItems(orderItems)
                .shippingAddress(
                        OrderConfirmationData.AddressData.builder()
                                .title(shippingAddress.getTitle())
                                .name(shippingAddress.getName())
                                .addressLine1(shippingAddress.getAddressLine1())
                                .addressLine2(shippingAddress.getAddressLine2())
                                .city(shippingAddress.getCity())
                                .country(shippingAddress.getCountry())
                                .build()
                )
                .guest(order.getCustomerUid() == null)
                .customerName(name)
                .siteName(siteName)
                .homePage(homePage)
                .registrationPage(registrationPage)
                .build();
    }
}
