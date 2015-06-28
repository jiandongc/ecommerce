package order.data;

import java.util.UUID;

public class CartSummaryData {

    private UUID cartUid;
    private int totalCount;
    private double totalPrice;

    public CartSummaryData(){}

    public CartSummaryData(UUID cartUid, int totalCount, double totalPrice){
        this.cartUid = cartUid;
        this.totalCount = totalCount;
        this.totalPrice = totalPrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
