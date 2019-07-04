package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {

    private Long customerId;

    private BigDecimal items;

    private BigDecimal postage;

    private BigDecimal promotion;

    private BigDecimal totalBeforeVat;

    private BigDecimal itemsVat;

    private BigDecimal postageVat;

    private BigDecimal promotionVat;

    private BigDecimal totalVat;

    private BigDecimal orderTotal;

    private String deliveryMethod;

    private Integer minDaysRequired;

    private Integer maxDaysRequired;

    private List<ItemData> orderItems;

    public void addOrderItem(ItemData itemData) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.add(itemData);
    }

}
