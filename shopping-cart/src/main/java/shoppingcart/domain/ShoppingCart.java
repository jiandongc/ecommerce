package shoppingcart.domain;

import java.util.*;

public final class ShoppingCart {

    private long id;
    private UUID cartUid;
    private Long customerId;
    private Date creationTime;
    private List<ShoppingCartItem> shoppingCartItems;
    private Address billingAddress;
    private Address shippingAddress;

    private ShoppingCart(long id, UUID cartUid, Long customerId, Date creationTime) {
        this.id = id;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.creationTime = creationTime;
    }

    public ShoppingCart(){}

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

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<ShoppingCartItem> getShoppingCartItems(){
        return shoppingCartItems;
    }

    public void addItem(ShoppingCartItem cartItem){
        if(shoppingCartItems == null){
            shoppingCartItems = new ArrayList<>();
        }
        shoppingCartItems.add(cartItem);
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(cartUid, that.cartUid) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(shoppingCartItems, that.shoppingCartItems) &&
                Objects.equals(billingAddress, that.billingAddress) &&
                Objects.equals(shippingAddress, that.shippingAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartUid, customerId, creationTime, shoppingCartItems, billingAddress, shippingAddress);
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
