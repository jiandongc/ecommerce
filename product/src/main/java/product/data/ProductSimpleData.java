package product.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSimpleData {

    private String name;

    private String code;

    private String imageUrl;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String discountRate;

    private boolean isOnSale;
}
