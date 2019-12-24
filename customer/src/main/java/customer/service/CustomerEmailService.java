package customer.service;

import customer.domain.Customer;
import email.data.PasswordResetData;
import email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomerEmailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UrlGenerator urlGenerator;

    public void sendPasswordResetEmail(Customer customer, String token){
        PasswordResetData passwordResetData = PasswordResetData.builder()
                .name(customer.getName())
                .link(urlGenerator.generatePasswordResetUrl(token))
                .sendTo(Arrays.asList(customer.getEmail()))
                .build();
        emailService.sendMessage(passwordResetData);
    }
}
