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

    private String code;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subTotal;

    private String imageUrl;

    private String thumbnail;

    private Integer vatRate;

    private BigDecimal vat;

    private BigDecimal sale;

}
