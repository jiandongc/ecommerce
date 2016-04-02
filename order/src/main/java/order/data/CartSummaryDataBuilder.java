package order.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jiandong on 31/03/16.
 */
public class CartSummaryDataBuilder {

    private UUID cartUid;
    private int totalCount;
    private double totalPrice;
    private Set<AnonCartItemData> cartItems = new HashSet<AnonCartItemData>();

    private CartSummaryDataBuilder(){}

    public static CartSummaryDataBuilder newBuilder(){
        return new CartSummaryDataBuilder();
    }

    public CartSummaryDataBuilder setCartUid(UUID cartUid) {
        this.cartUid = cartUid;
        return this;
    }

    public CartSummaryDataBuilder setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public CartSummaryDataBuilder setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public CartSummaryDataBuilder addAnonCartItemData(AnonCartItemData anonCartItemData){
        this.cartItems.add(anonCartItemData);
        return this;
    }

    public CartSummaryData build(){
        return new CartSummaryData(this.cartUid, this.totalCount, this.totalPrice, this.cartItems);
    }
}
