package order.data;

import java.util.UUID;

public class AnonCartItemData {

    private UUID cartUid;
    private long productId;
    private String productName;
    private double productPrice;
    private int quantity;

    public AnonCartItemData(){}

    public AnonCartItemData(UUID cartUid, long productId, String productName, double productPrice, int quantity){
        this.cartUid = cartUid;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
