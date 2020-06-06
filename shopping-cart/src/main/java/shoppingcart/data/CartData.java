package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CartData {

    private Integer quantity;

    private BigDecimal itemsTotal;

    private BigDecimal postage;

    private BigDecimal promotion;

    private BigDecimal vat;

    private BigDecimal orderTotal;

    private String cartUid;

    private String customerId;

    private String email;

    private List<ItemData> cartItems;

    private AddressData shipping;

    private AddressData billing;

    private DeliveryOptionData deliveryOption;
}
