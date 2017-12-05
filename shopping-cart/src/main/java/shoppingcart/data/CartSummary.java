package shoppingcart.data;

import shoppingcart.domain.ShoppingCart;

import java.math.BigDecimal;

public class CartSummary {

    private int totalQuantity;
    private BigDecimal itemsSubTotal;
    private ShoppingCart shoppingCart;

    public CartSummary(int totalQuantity,
                       BigDecimal itemsSubTotal,
                       ShoppingCart shoppingCart){
        this.totalQuantity = totalQuantity;
        this.itemsSubTotal = itemsSubTotal;
        this.shoppingCart = shoppingCart;
    }

    public CartSummary(){}

    public static CartSummary.CartSummaryBuilder builder(){
        return new CartSummary.CartSummaryBuilder();
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getItemsSubTotal() {
        return itemsSubTotal;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartSummary that = (CartSummary) o;

        if (totalQuantity != that.totalQuantity) return false;
        if (itemsSubTotal != null ? !itemsSubTotal.equals(that.itemsSubTotal) : that.itemsSubTotal != null)
            return false;
        return shoppingCart != null ? shoppingCart.equals(that.shoppingCart) : that.shoppingCart == null;
    }

    @Override
    public int hashCode() {
        int result = totalQuantity;
        result = 31 * result + (itemsSubTotal != null ? itemsSubTotal.hashCode() : 0);
        result = 31 * result + (shoppingCart != null ? shoppingCart.hashCode() : 0);
        return result;
    }

    public static class CartSummaryBuilder{
        private int totalQuantity;
        private BigDecimal itemsSubTotal;
        private ShoppingCart shoppingCart;

        public CartSummaryBuilder totalQuantity(int totalQuantity){
            this.totalQuantity = totalQuantity;
            return this;
        }

        public CartSummaryBuilder itemsSubTotal(BigDecimal itemsSubTotal){
            this.itemsSubTotal = itemsSubTotal;
            return this;
        }

        public CartSummaryBuilder shoppingCart(ShoppingCart shoppingCart){
            this.shoppingCart = shoppingCart;
            return this;
        }

        public CartSummary build(){
            return new CartSummary(totalQuantity, itemsSubTotal, shoppingCart);
        }
    }
}
