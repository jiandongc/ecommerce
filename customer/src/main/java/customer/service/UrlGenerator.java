package customer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlGenerator {

    @Value("${host.url}")
    private String hostUrl;

    public String generatePasswordResetUrl(String token){
        return String.format("%s/customer/password?token=%s", hostUrl, token);
    }
}
