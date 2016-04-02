package order.data;

import java.util.Set;
import java.util.UUID;

public class CartSummaryData {

    private UUID cartUid;
    private int totalCount;
    private double totalPrice;
    private Set<AnonCartItemData> cartItems;

    public CartSummaryData(UUID cartUid, int totalCount, double totalPrice, Set<AnonCartItemData> cartItems) {
        this.cartUid = cartUid;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
        this.cartItems = cartItems;
    }

    public CartSummaryData(){}

    public int getTotalCount() {
        return totalCount;
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Set<AnonCartItemData> getCartItems() {
        return cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartSummaryData that = (CartSummaryData) o;

        if (totalCount != that.totalCount) return false;
        if (Double.compare(that.totalPrice, totalPrice) != 0) return false;
        if (cartUid != null ? !cartUid.equals(that.cartUid) : that.cartUid != null) return false;
        return !(cartItems != null ? !cartItems.equals(that.cartItems) : that.cartItems != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + totalCount;
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (cartItems != null ? cartItems.hashCode() : 0);
        return result;
    }
}
