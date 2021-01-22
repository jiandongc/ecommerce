package shoppingcart.service;

import email.data.WelcomeEmailData;
import email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class ShoppingCartEmailService {

    @Autowired
    private EmailService emailService;

    @Value("${admin.emails}")
    private String adminEmails;

    public void sendWelcomeEmail(String email, String voucherAmount, String voucherCode){
        WelcomeEmailData welcomeEmailData = WelcomeEmailData.builder()
                .sendTo(Arrays.asList(email))
                .bccTo(Stream.of(adminEmails.split(";")).collect(toList()))
                .voucherAmount(voucherAmount)
                .voucherCode(voucherCode)
                .build();
        emailService.sendMessage(welcomeEmailData);
    }
}
