package shoppingcart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/carts/sage")
public class SagePayController {

    @Value("${sage.resourceUrl}")
    private String resourceUrl;

    @Value("${sage.vendorName}")
    private String vendorName;

    @Value("${sage.integrationKey}")
    private String integrationKey;

    @Value("${sage.integrationPassword}")
    private String integrationPassword;


    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String downloadMerchantKey() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationString());
        final HttpEntity<Object> payload = new HttpEntity<>(format("{\"vendorName\": \"%s\"}",vendorName), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(resourceUrl, payload, String.class);
        return responseEntity.getBody();
    }

    private String getAuthorizationString(){
        String integrationString = String.format("%s:%s", integrationKey, integrationPassword);
        return "Basic " + Base64.getEncoder().encodeToString(integrationString.getBytes());
    }

}
