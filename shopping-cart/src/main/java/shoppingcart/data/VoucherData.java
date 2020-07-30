package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherData {

    private String code;

    private String name;

    private BigDecimal discountAmount;

    private String startDate;

    private String endDate;

    private String creationTime;

}
