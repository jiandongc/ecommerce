package shoppingcart.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DeliveryOption {

    private Long id;

    private Long cartId;

    private String method;

    private Double charge;

    private Integer minDaysRequired;

    private Integer maxDaysRequired;

    private Date creationTime;

    private Date lastUpdateTime;

}
