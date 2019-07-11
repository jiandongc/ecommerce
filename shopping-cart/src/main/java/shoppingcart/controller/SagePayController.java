package shoppingcart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import shoppingcart.data.sage.Transaction;
import shoppingcart.domain.ShoppingCart;
import shoppingcart.service.ShoppingCartService;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@RequestMapping("/carts/sage")
public class SagePayController {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private final ShoppingCartService cartService;

    @Value("${sage.merchantKeyUrl}")
    private String merchantKeyUrl;

    @Value("${sage.transactionSubmissionUrl}")
    private String transactionSubmissionUrl;

    @Value("${sage.vendorName}")
    private String vendorName;

    @Value("${sage.integrationKey}")
    private String integrationKey;

    @Value("${sage.integrationPassword}")
    private String integrationPassword;

    @Autowired
    public SagePayController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public String downloadMerchantKey() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationString());
        final HttpEntity<Object> payload = new HttpEntity<>(format("{\"vendorName\": \"%s\"}", vendorName), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(merchantKeyUrl, payload, String.class);
        return responseEntity.getBody();
    }

    @PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(method = POST, value = "transactions/{cartUid}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitTransaction(@PathVariable UUID cartUid, @RequestBody Transaction.Card card) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getAuthorizationString());

        Optional<ShoppingCart> cartOptional = cartService.getShoppingCartByUid(cartUid);
        Transaction transaction = cartOptional.map(cart ->
                Transaction.builder()
                        .transactionType("Payment")
                        .vendorTxCode(cartUid.toString())
                        .amount(cart.getOrderTotal().multiply(new BigDecimal("100")).intValueExact())
                        .currency("GBP")
                        .customerFirstName(cart.getShippingAddress().getName())
                        .customerLastName(cart.getBillingAddress().getName())
                        .description("description")
                        .apply3DSecure("Disable")
                        .paymentMethod(Transaction.PaymentMethod.builder()
                                .card(Transaction.Card.builder()
                                        .cardIdentifier(card.getCardIdentifier())
                                        .merchantSessionKey(card.getMerchantSessionKey())
                                        .build())
                                .build())
                        .billingAddress(Transaction.BillingAddress.builder()
                                .address1(cart.getBillingAddress().getAddressLine1())
                                .address2(cart.getBillingAddress().getAddressLine2())
                                .city(cart.getBillingAddress().getCity())
                                .country("GB")
                                .postalCode(cart.getBillingAddress().getPostcode())
                                .build())
                        .build()
        ).orElseThrow(() -> new RuntimeException(String.format("CartUid : %s not found", cartUid)));

        try {
            final HttpEntity<Transaction> payload = new HttpEntity<>(transaction, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(transactionSubmissionUrl, payload, String.class);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.CREATED);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Failed to submit transaction with error: {}", e.getResponseBodyAsString(), e);
            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAuthorizationString() {
        String integrationString = String.format("%s:%s", integrationKey, integrationPassword);
        return "Basic " + Base64.getEncoder().encodeToString(integrationString.getBytes());
    }

}
