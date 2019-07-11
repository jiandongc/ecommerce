package shoppingcart.data.sage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private String transactionType;

    private String vendorTxCode;

    private Integer amount;

    private String currency;

    private String description;

    private String apply3DSecure;

    private String customerFirstName;

    private String customerLastName;

    private PaymentMethod paymentMethod;

    private BillingAddress billingAddress;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethod {

        private Card card;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Card {

        private String merchantSessionKey;

        private String cardIdentifier;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingAddress {

        private String address1;

        private String address2;

        private String city;

        private String postalCode;

        private String country;

    }



}
