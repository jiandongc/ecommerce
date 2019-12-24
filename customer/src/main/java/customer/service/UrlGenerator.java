package customer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlGenerator {

    @Value("${web.base.url}")
    private String webBaseUrl;

    public String generatePasswordResetUrl(String token){
        return String.format("%s/#!/login/password?token=%s", webBaseUrl, token);
    }
}
