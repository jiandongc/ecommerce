package shoppingcart.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class ShoppingCart {

    private final long id;
    private final UUID cartUid;
    private final Long customerId;
    private final Date creationTime;
    private List<ShoppingCartItem> shoppingCartItems;

    private ShoppingCart(long id, UUID cartUid, Long customerId, Date creationTime) {
        this.id = id;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.creationTime = creationTime;
    }

    public static ShoppingCartBuilder builder(){
        return new ShoppingCartBuilder();
    }

    public long getId() {
        return id;
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public List<ShoppingCartItem> getShoppingCartItems(){
        return shoppingCartItems;
    }

    public void addItem(ShoppingCartItem shoppingCartItem){
        if(shoppingCartItems == null){
            shoppingCartItems = new ArrayList<>();
        }
        shoppingCartItems.add(shoppingCartItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (cartUid != null ? !cartUid.equals(that.cartUid) : that.cartUid != null) return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        return shoppingCartItems != null ? shoppingCartItems.equals(that.shoppingCartItems) : that.shoppingCartItems == null;
    }

    @Override
    public int hashCode() {
        int result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (shoppingCartItems != null ? shoppingCartItems.hashCode() : 0);
        return result;
    }

    public static class ShoppingCartBuilder {
        private long id;
        private UUID cartUid;
        private Long customerId;
        private Date creationTime;

        public ShoppingCartBuilder id(long id){
            this.id = id;
            return this;
        }

        public ShoppingCartBuilder cartUid(UUID cartUid){
            this.cartUid = cartUid;
            return this;
        }

        public ShoppingCartBuilder customerId(Long customerId){
            this.customerId = customerId;
            return this;
        }

        public ShoppingCartBuilder creationTime(Date creationTime){
            this.creationTime = creationTime;
            return this;
        }

        public ShoppingCart build(){
            return new ShoppingCart(id, cartUid, customerId, creationTime);
        }



    }
}
