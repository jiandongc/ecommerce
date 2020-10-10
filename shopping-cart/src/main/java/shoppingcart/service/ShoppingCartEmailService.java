package shoppingcart.service;

import email.data.WelcomeEmailData;
import email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ShoppingCartEmailService {

    @Autowired
    private EmailService emailService;

    public void sendWelcomeEmail(String email, String voucherAmount, String voucherCode){
        WelcomeEmailData welcomeEmailData = WelcomeEmailData.builder()
                .sendTo(Arrays.asList(email))
                .voucherAmount(voucherAmount)
                .voucherCode(voucherCode)
                .build();
        emailService.sendMessage(welcomeEmailData);
    }
}
