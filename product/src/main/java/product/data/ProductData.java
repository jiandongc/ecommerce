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

    private String name;

    private String description;

    private String categoryCode;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String discountRate;

    private boolean isOnSale;

    private Map<String, Set<String>> attributes;

    private List<Map<String, String>> variants;

    private Map<String, List<String>> images;

    public void addAttribute(String key, String value) {
        if (attributes.containsKey(key)) {
            attributes.get(key).add(value);
        } else {
            final Set<String> values = new LinkedHashSet<>();
            values.add(value);
            attributes.put(key, values);
        }

    }

    public void addVariant(Map<String, String> variant) {
        variants.add(variant);
    }

    public void addImage(String key, String value) {
        if (images.containsKey(key)) {
            images.get(key).add(value);
        } else {
            final List<String> values = new ArrayList<>();
            values.add(value);
            images.put(key, values);
        }
    }


}
