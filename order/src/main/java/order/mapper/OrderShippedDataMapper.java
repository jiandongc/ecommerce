package order.mapper;

import email.data.OrderShippedData;
import order.domain.Order;
import order.domain.OrderAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class OrderShippedDataMapper {

    @Value("${admin.emails}")
    private String adminEmails;

    public OrderShippedData map(Order order) {
        OrderAddress shippingAddress = order.getShippingAddress().get();
        List<OrderShippedData.OrderItemData> orderItems = order.getOrderItems().stream()
                .map(item -> OrderShippedData.OrderItemData.builder()
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
        return OrderShippedData.builder()
                .sendTo(Arrays.asList(order.getEmail()))
                .bccTo(Stream.of(adminEmails.split(";")).collect(toList()))
                .orderNumber(order.getOrderNumber())
                .orderDeliveryMethod(order.getDeliveryMethod())
                .orderItems(orderItems)
                .shippingAddress(
                        OrderShippedData.AddressData.builder()
                                .title(shippingAddress.getTitle())
                                .name(shippingAddress.getName())
                                .addressLine1(shippingAddress.getAddressLine1())
                                .addressLine2(shippingAddress.getAddressLine2())
                                .city(shippingAddress.getCity())
                                .country(shippingAddress.getCountry())
                                .build()
                ).build();
    }
}
