package shoppingcart.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class CartData {

    private final Integer quantity;
    private final Double subTotal;
    private final String cartUid;
    private final Long customerId;
    private final List<CartItemData> cartItems;
    private final AddressData shipping;
    private final AddressData billing;

    @JsonCreator
    private CartData(@JsonProperty("quantity") Integer quantity,
                     @JsonProperty("subTotal") Double subTotal,
                     @JsonProperty("cartUid") String cartUid,
                     @JsonProperty("customerId") Long customerId,
                     @JsonProperty("cartItems") List<CartItemData> cartItems,
                     @JsonProperty("shipping") AddressData shipping,
                     @JsonProperty("billing") AddressData billing) {
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.cartItems = cartItems;
        this.shipping = shipping;
        this.billing = billing;
    }

    public static Builder builder(){
        return new Builder();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public String getCartUid() {
        return cartUid;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<CartItemData> getCartItems() {
        return cartItems;
    }

    public AddressData getShipping() {
        return shipping;
    }

    public AddressData getBilling() {
        return billing;
    }

    public static class Builder {
        private Integer quantity;
        private Double subTotal;
        private String cartUid;
        private Long customerId;
        private List<CartItemData> cartItems;
        private AddressData shipping;
        private AddressData billing;

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder subTotal(Double subTotal) {
            this.subTotal = subTotal;
            return this;
        }

        public Builder cartUid(String cartUid) {
            this.cartUid = cartUid;
            return this;
        }

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder cartItems(List<CartItemData> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Builder shipping(AddressData shipping) {
            this.shipping = shipping;
            return this;
        }

        public Builder billing(AddressData billing) {
            this.billing = billing;
            return this;
        }

        public CartData build(){
            return new CartData(quantity, subTotal, cartUid, customerId, cartItems, shipping, billing);
        }
    }
}
