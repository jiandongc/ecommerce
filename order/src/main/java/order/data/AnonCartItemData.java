package order.data;

import java.util.UUID;

public class AnonCartItemData {

    private UUID cartUid;
    private long productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private double subTotal;
    private String imageUrl;

    public AnonCartItemData(UUID cartUid, long productId, String productName, double productPrice, int quantity, double subTotal, String imageUrl){
        this.cartUid = cartUid;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public double getSubTotal() {
        return subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnonCartItemData that = (AnonCartItemData) o;

        if (productId != that.productId) return false;
        if (Double.compare(that.productPrice, productPrice) != 0) return false;
        if (quantity != that.quantity) return false;
        if (Double.compare(that.subTotal, subTotal) != 0) return false;
        if (cartUid != null ? !cartUid.equals(that.cartUid) : that.cartUid != null) return false;
        if (productName != null ? !productName.equals(that.productName) : that.productName != null) return false;
        return !(imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null);

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
        temp = Double.doubleToLongBits(subTotal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
