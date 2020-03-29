package order.configuration;

import email.service.EmailService;
import email.service.EmailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailConfiguration {

    @Bean
    public EmailService emailService(JavaMailSender javaMailSender){
        return new EmailServiceImpl(javaMailSender);
    }
}
