package order.mapper;

import email.data.GoogleReviewRequestData;
import order.domain.Order;
import order.domain.OrderAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class GoogleReviewRequestDataMapper {

    @Value("${admin.emails}")
    private String adminEmails;

    public GoogleReviewRequestData map(Order order, String voucherCode) {
        OrderAddress shippingAddress = order.getShippingAddress().get();
        return GoogleReviewRequestData.builder()
                .sendTo(Arrays.asList(order.getEmail()))
                .bccTo(Stream.of(adminEmails.split(";")).collect(toList()))
                .customerName(shippingAddress.getName())
                .voucherCode(voucherCode)
                .build();
    }
}
