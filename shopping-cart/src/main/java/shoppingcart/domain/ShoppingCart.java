package shoppingcart.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

public class ShoppingCart {

    private long id;
    private UUID cartUid;
    private UUID customerUid;
    private String email;
    private Date creationTime;
    private boolean active;
    private List<ShoppingCartItem> shoppingCartItems;
    private Address billingAddress;
    private Address shippingAddress;
    private DeliveryOption deliveryOption;
    private Promotion promotion;

    private ShoppingCart(long id, UUID cartUid, UUID customerUid, String email, Date creationTime, boolean active) {
        this.id = id;
        this.cartUid = cartUid;
        this.customerUid = customerUid;
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

    public UUID getCustomerUid() {
        return customerUid;
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

    public void setCustomerUid(UUID customerUid) {
        this.customerUid = customerUid;
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

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public BigDecimal getItemSubTotal() {
        return this.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getItemTotal)
                .reduce(ZERO.setScale(2, ROUND_HALF_UP), BigDecimal::add);
    }

    public BigDecimal getItemsVat() {
        return this.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getVat)
                .reduce(ZERO.setScale(2, ROUND_HALF_UP), BigDecimal::add);
    }

    public BigDecimal getItemsBeforeVat() {
        return this.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getSale)
                .reduce(ZERO.setScale(2, ROUND_HALF_UP), BigDecimal::add);
    }

    public BigDecimal getPostage() {
        if (this.getDeliveryOption() != null && this.getDeliveryOption().getCharge() != null) {
            return BigDecimal.valueOf(this.getDeliveryOption().getCharge()).setScale(2, ROUND_HALF_UP);
        } else {
            return ZERO.setScale(2, ROUND_HALF_UP);
        }
    }

    public BigDecimal getPostageVat() {
        return this.getPostage().subtract(this.getPostageBeforeVat());
    }

    public BigDecimal getPostageBeforeVat() {
        int postageVatRate = this.getDeliveryOption() == null ? 0 : this.getDeliveryOption().getVatRate();
        double divisor = (double) postageVatRate / 100 + 1;
        return getPostage().divide(BigDecimal.valueOf(divisor), 2, ROUND_HALF_UP);
    }

    public BigDecimal getDiscount() {
        if (this.getPromotion() != null && this.getPromotion().getDiscountAmount() != null) {
            return this.getPromotion().getDiscountAmount().setScale(2, ROUND_HALF_UP);
        } else {
            return ZERO.setScale(2, ROUND_HALF_UP);
        }
    }

    public BigDecimal getDiscountBeforeVat() {
        return getDiscount().divide(getDiscountVatRate(), 2, ROUND_HALF_UP);
    }

    public BigDecimal getDiscountVat() {
        return this.getDiscount().subtract(this.getDiscountBeforeVat());
    }

    private BigDecimal getDiscountVatRate() {
        BigDecimal vat = getItemsVat().add(getPostageVat());
        BigDecimal total = getItemSubTotal().add(getPostage());
        return vat.divide(total, 2, ROUND_HALF_UP).add(BigDecimal.ONE);
    }

    public BigDecimal getOrderTotal() {
        BigDecimal itemSubTotal = getItemSubTotal();
        BigDecimal postage = getPostage();
        BigDecimal discountAmount = getDiscount();
        return itemSubTotal.add(postage).subtract(discountAmount);
    }

    public BigDecimal getVatTotal() {
        BigDecimal itemsVat = getItemsVat();
        BigDecimal postageVat = getPostageVat();
        BigDecimal discountVat = getDiscountVat();
        return itemsVat.add(postageVat).subtract(discountVat);
    }

    public static class ShoppingCartBuilder {
        private long id;
        private UUID cartUid;
        private UUID customerUid;
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

        public ShoppingCartBuilder customerUid(UUID customerUid) {
            this.customerUid = customerUid;
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
            return new ShoppingCart(id, cartUid, customerUid, email, creationTime, active);
        }

    }
}
