package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class CartData {

    private Integer quantity;

    private Double subTotal;

    private String cartUid;

    private Long customerId;

    private List<ItemData> cartItems;

    private AddressData shipping;

    private AddressData billing;

    private DeliveryOptionData deliveryOption;
}
