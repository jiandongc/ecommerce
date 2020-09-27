package product.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    private List<Map<String, String>> tags;
}
