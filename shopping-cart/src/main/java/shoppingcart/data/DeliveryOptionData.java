package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class DeliveryOptionData {

    private String method;

    private Double charge;

    private Integer minDaysRequired;

    private Integer maxDaysRequired;

    private String eta;

    private Integer vatRate;
}
