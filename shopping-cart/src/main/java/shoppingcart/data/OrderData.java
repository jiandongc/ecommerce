package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {

    private String customerId;

    private String email;

    private BigDecimal items;

    private BigDecimal postage;

    private BigDecimal discount;

    private BigDecimal totalBeforeVat;

    private BigDecimal itemsVat;

    private BigDecimal postageVat;

    private BigDecimal discountVat;

    private BigDecimal totalVat;

    private BigDecimal orderTotal;

    private String deliveryMethod;

    private Integer minDaysRequired;

    private Integer maxDaysRequired;

    private List<ItemData> orderItems;

    private List<AddressData> orderAddresses;

    public void addOrderItem(ItemData itemData) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.add(itemData);
    }

    public void addOrderAddresses(AddressData addressData) {
        if (this.orderAddresses == null) {
            this.orderAddresses = new ArrayList<>();
        }

        this.orderAddresses.add(addressData);
    }

}
