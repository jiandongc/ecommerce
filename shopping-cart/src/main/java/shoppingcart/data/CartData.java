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

    @JsonCreator
    private CartData(@JsonProperty("quantity") Integer quantity,
                     @JsonProperty("subTotal") Double subTotal,
                     @JsonProperty("cartUid") String cartUid,
                     @JsonProperty("customerId") Long customerId,
                     @JsonProperty("cartItems") List<CartItemData> cartItems) {
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.cartItems = cartItems;
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

    public static class Builder {
        private Integer quantity;
        private Double subTotal;
        private String cartUid;
        private Long customerId;
        private List<CartItemData> cartItems;

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

        public CartData build(){
            return new CartData(quantity, subTotal, cartUid, customerId, cartItems);
        }
    }
}
