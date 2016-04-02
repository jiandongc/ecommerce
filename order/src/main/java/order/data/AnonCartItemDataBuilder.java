package order.data;

import java.util.UUID;

/**
 * Created by jiandong on 31/03/16.
 */
public class AnonCartItemDataBuilder {

    private UUID cartUid;
    private long productId;
    private String productName;
    private double productPrice;
    private int quantity;

    private AnonCartItemDataBuilder(){}

    public static AnonCartItemDataBuilder newBuilder(){
        return new AnonCartItemDataBuilder();
    }

    public AnonCartItemDataBuilder setCartUid(UUID cartUid) {
        this.cartUid = cartUid;
        return this;
    }

    public AnonCartItemDataBuilder setProductId(long productId) {
        this.productId = productId;
        return this;
    }

    public AnonCartItemDataBuilder setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public AnonCartItemDataBuilder setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public AnonCartItemDataBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public AnonCartItemData build(){
        return new AnonCartItemData(
                this.cartUid, this.productId, this.productName, this.productPrice, this.quantity);
    }
}
