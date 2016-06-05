package order.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "anon_cart")
public class AnonCart {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "anon_cart_seq", sequenceName = "anon_cart_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anon_cart_seq")
    private long id;

    @Column(name = "cart_uid")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    private UUID cartUid;

    @Column(name = "customer_id")
    private Long customerId;

    @OneToMany(mappedBy = "anonCart", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    private Set<AnonCartItem> anonCartItems;

    public AnonCart() {
        this.cartUid = randomUUID();
        this.anonCartItems = new HashSet<AnonCartItem>();
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Set<AnonCartItem> getAnonCartItems() {
        return Collections.unmodifiableSet(anonCartItems);
    }

    public void addAnonCartItem(AnonCartItem anonCartItem) {
        anonCartItem.setAnonCart(this);
    }

    public void removeAnonCartItem(AnonCartItem anonCartItem) {
        anonCartItem.setAnonCart(null);
    }

    public void internalAddAnonCartItem(AnonCartItem anonCartItem) {
        this.anonCartItems.add(anonCartItem);
    }

    public void internalRemoveAnonCartItem(AnonCartItem anonCartItem) {
        this.anonCartItems.remove(anonCartItem);
    }

    public double getTotalPrice() {
        double totalPrice = 0D;
        for (AnonCartItem anonCartItem : anonCartItems) {
            totalPrice += anonCartItem.getSubTotal();
        }
        return totalPrice;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (AnonCartItem anonCartItem : anonCartItems) {
            totalQuantity += anonCartItem.getQuantity();
        }
        return totalQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnonCart anonCart = (AnonCart) o;

        if (cartUid != null ? !cartUid.equals(anonCart.cartUid) : anonCart.cartUid != null) return false;
        if (customerId != null ? !customerId.equals(anonCart.customerId) : anonCart.customerId != null) return false;
        return !(anonCartItems != null ? !anonCartItems.equals(anonCart.anonCartItems) : anonCart.anonCartItems != null);

    }

    @Override
    public int hashCode() {
        int result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (anonCartItems != null ? anonCartItems.hashCode() : 0);
        return result;
    }
}
