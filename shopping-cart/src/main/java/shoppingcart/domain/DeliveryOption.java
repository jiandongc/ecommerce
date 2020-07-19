package shoppingcart.domain;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOption {

    private Long id;

    private Long cartId;

    private String method;

    private Double charge;

    private Integer minDaysRequired;

    private Integer maxDaysRequired;

    private Date creationTime;

    private Date lastUpdateTime;

    private Integer vatRate;

}
