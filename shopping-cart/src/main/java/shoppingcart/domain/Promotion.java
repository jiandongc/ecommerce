package shoppingcart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    private Long id;

    private String voucherCode;

    private Voucher.Type voucherType;

    private BigDecimal discountAmount;

    private Integer vatRate;

    private Date creationTime;

    private Date lastUpdateTime;

    private Long cartId;

}
