package shoppingcart.domain;

import java.math.BigDecimal;
import java.util.*;

public class ShoppingCart {

    private long id;
    private UUID cartUid;
    private Long customerId;
    private Date creationTime;
    private List<ShoppingCartItem> shoppingCartItems;
    private Address billingAddress;
    private Address shippingAddress;
    private DeliveryOption deliveryOption;

    private ShoppingCart(long id, UUID cartUid, Long customerId, Date creationTime) {
        this.id = id;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.creationTime = creationTime;
    }

    public ShoppingCart() {
    }

    public static ShoppingCartBuilder builder() {
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

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void addItem(ShoppingCartItem cartItem) {
        if (shoppingCartItems == null) {
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

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public BigDecimal getItemSubTotal() {
        double itemSubTotal = this.getShoppingCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();
        return BigDecimal.valueOf(itemSubTotal);
    }

    public BigDecimal getItemsVat() {
        BigDecimal itemSubTotal = getItemSubTotal();
        BigDecimal itemsBeforeVat = itemSubTotal.divide(new BigDecimal(1.2), 2, BigDecimal.ROUND_HALF_EVEN);
        return itemSubTotal.subtract(itemsBeforeVat);
    }

    public BigDecimal getPostage() {
        if (this.getDeliveryOption() != null && this.getDeliveryOption().getCharge() != null) {
            return BigDecimal.valueOf(this.getDeliveryOption().getCharge());
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPostageVat(){
        BigDecimal postage = getPostage();
        BigDecimal postageBeforeVat = postage.divide(new BigDecimal(1.2), 2, BigDecimal.ROUND_HALF_EVEN);
        return postage.subtract(postageBeforeVat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (cartUid != null ? !cartUid.equals(that.cartUid) : that.cartUid != null) return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        if (shoppingCartItems != null ? !shoppingCartItems.equals(that.shoppingCartItems) : that.shoppingCartItems != null)
            return false;
        if (billingAddress != null ? !billingAddress.equals(that.billingAddress) : that.billingAddress != null)
            return false;
        if (shippingAddress != null ? !shippingAddress.equals(that.shippingAddress) : that.shippingAddress != null)
            return false;
        return deliveryOption != null ? deliveryOption.equals(that.deliveryOption) : that.deliveryOption == null;
    }

    @Override
    public int hashCode() {
        int result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (shoppingCartItems != null ? shoppingCartItems.hashCode() : 0);
        result = 31 * result + (billingAddress != null ? billingAddress.hashCode() : 0);
        result = 31 * result + (shippingAddress != null ? shippingAddress.hashCode() : 0);
        result = 31 * result + (deliveryOption != null ? deliveryOption.hashCode() : 0);
        return result;
    }

    public static class ShoppingCartBuilder {
        private long id;
        private UUID cartUid;
        private Long customerId;
        private Date creationTime;

        public ShoppingCartBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ShoppingCartBuilder cartUid(UUID cartUid) {
            this.cartUid = cartUid;
            return this;
        }

        public ShoppingCartBuilder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public ShoppingCartBuilder creationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public ShoppingCart build() {
            return new ShoppingCart(id, cartUid, customerId, creationTime);
        }

    }
}
