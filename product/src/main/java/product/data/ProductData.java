package product.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {

    private String code;

    private String type;

    private String name;

    private String description;

    private String shortDescription;

    private String categoryCode;

    private Integer vat;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String discountRate;

    private boolean isOnSale;

    private String saleEndDate;

    private Map<String, Set<String>> attributes;

    private List<Map<String, Object>> variants;

    private List<String> images;

    private Map<String, String> brand;

    private List<Map<String, String>> tags;

}
