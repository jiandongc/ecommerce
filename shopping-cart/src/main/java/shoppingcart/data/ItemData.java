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
public final class ItemData {

    private String sku;

    private String name;

    private BigDecimal price;

    private Integer quantity;

    private String thumbnail;

    private String description;

    private String code;

    private String imageUrl;

    private BigDecimal subTotal;

    private Integer vatRate;
}
