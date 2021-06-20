package shoppingcart.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class DeliveryOptionOffer extends DeliveryOption {

    @Builder(builderMethodName = "deliveryOptionOfferBuilder")
    public DeliveryOptionOffer(
            Long id,
            Long cartId,
            String method,
            Double charge,
            Integer minDaysRequired,
            Integer maxDaysRequired,
            Date creationTime,
            Date lastUpdateTime,
            Integer vatRate,
            String countryCode,
            Double minSpend,
            LocalDate startDate,
            LocalDate endDate) {
        super(id, cartId, method, charge, minDaysRequired, maxDaysRequired, creationTime, lastUpdateTime, vatRate);
        this.countryCode = countryCode;
        this.minSpend = minSpend;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private String countryCode;

    private Double minSpend;

    private LocalDate startDate;

    private LocalDate endDate;

}
