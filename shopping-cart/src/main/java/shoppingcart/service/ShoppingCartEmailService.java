package shoppingcart.service;

import email.data.AbandonedCartNotificationData;
import email.data.WelcomeEmailData;
import email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shoppingcart.domain.ShoppingCartItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class ShoppingCartEmailService {

    @Autowired
    private EmailService emailService;

    @Value("${admin.emails}")
    private String adminEmails;

    public void sendWelcomeEmail(String email, String voucherAmount, String voucherCode) {
        WelcomeEmailData welcomeEmailData = WelcomeEmailData.builder()
                .sendTo(Arrays.asList(email))
                .bccTo(Stream.of(adminEmails.split(";")).collect(toList()))
                .voucherAmount(voucherAmount)
                .voucherCode(voucherCode)
                .build();
        emailService.sendMessage(welcomeEmailData);
    }

    public void sendAbandonedCartNotification(String email, String cartUid, List<ShoppingCartItem> shoppingCartItems) {
        List<AbandonedCartNotificationData.CartItemData> cartItemData = shoppingCartItems.stream()
                .map(cartItem -> AbandonedCartNotificationData.CartItemData.builder()
                        .name(cartItem.getName())
                        .quantity(String.valueOf(cartItem.getQuantity()))
                        .description(cartItem.getDescription())
                        .imageUrl(cartItem.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        AbandonedCartNotificationData abandonedCartNotificationData = AbandonedCartNotificationData.builder()
                .sendTo(Arrays.asList(email))
                .bccTo(Stream.of(adminEmails.split(";")).collect(toList()))
                .cartUid(cartUid)
                .cartItems(cartItemData)
                .build();

        emailService.sendMessage(abandonedCartNotificationData);

    }
}
