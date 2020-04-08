package shoppingcart.domain;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

public class ShoppingCart {

    private long id;
    private UUID cartUid;
    private Long customerId;
    private String email;
    private Date creationTime;
    private boolean active;
    private List<ShoppingCartItem> shoppingCartItems;
    private Address billingAddress;
    private Address shippingAddress;
    private DeliveryOption deliveryOption;

    private ShoppingCart(long id, UUID cartUid, Long customerId, String email, Date creationTime, boolean active) {
        this.id = id;
        this.cartUid = cartUid;
        this.customerId = customerId;
        this.email = email;
        this.creationTime = creationTime;
        this.active = active;
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

    public String getEmail() {
        return email;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public boolean isActive() {
        return active;
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
        return this.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getItemTotal)
                .reduce(ZERO.setScale(2, ROUND_HALF_UP), BigDecimal::add);
    }

    public BigDecimal getItemsVat() {
        BigDecimal itemSubTotal = getItemSubTotal();
        BigDecimal itemsBeforeVat = itemSubTotal.divide(new BigDecimal(1.2), 2, ROUND_HALF_UP);
        return itemSubTotal.subtract(itemsBeforeVat);
    }

    public BigDecimal getPostage() {
        if (this.getDeliveryOption() != null && this.getDeliveryOption().getCharge() != null) {
            return BigDecimal.valueOf(this.getDeliveryOption().getCharge()).setScale(2, ROUND_HALF_UP);
        } else {
            return ZERO.setScale(2, ROUND_HALF_UP);
        }
    }

    public BigDecimal getPostageVat() {
        BigDecimal postage = getPostage();
        BigDecimal postageBeforeVat = postage.divide(new BigDecimal(1.2), 2, ROUND_HALF_UP);
        return postage.subtract(postageBeforeVat);
    }

    public BigDecimal getOrderTotal() {
        BigDecimal itemSubTotal = getItemSubTotal();
        BigDecimal postage = getPostage();
        return itemSubTotal.add(postage);
    }

    public static class ShoppingCartBuilder {
        private long id;
        private UUID cartUid;
        private Long customerId;
        private String email;
        private Date creationTime;
        private boolean active;

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

        public ShoppingCartBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ShoppingCartBuilder creationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public ShoppingCartBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public ShoppingCart build() {
            return new ShoppingCart(id, cartUid, customerId, email, creationTime, active);
        }

    }
}
