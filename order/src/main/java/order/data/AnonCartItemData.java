package order.data;

import java.util.UUID;

public class AnonCartItemData {

    private UUID cartUid;
    private long productId;
    private String productName;
    private double productPrice;
    private int quantity;

    public AnonCartItemData(UUID cartUid, long productId, String productName, double productPrice, int quantity){
        this.cartUid = cartUid;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public AnonCartItemData(){}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnonCartItemData that = (AnonCartItemData) o;

        if (productId != that.productId) return false;
        if (Double.compare(that.productPrice, productPrice) != 0) return false;
        if (quantity != that.quantity) return false;
        if (cartUid != null ? !cartUid.equals(that.cartUid) : that.cartUid != null) return false;
        return !(productName != null ? !productName.equals(that.productName) : that.productName != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        temp = Double.doubleToLongBits(productPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantity;
        return result;
    }
}
