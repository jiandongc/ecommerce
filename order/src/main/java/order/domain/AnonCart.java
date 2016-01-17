package order.domain;

import javax.persistence.*;
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
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID cartUid;

    @Column(name = "customer_id")
    private Long customerId;

    @OneToMany(fetch = LAZY, cascade= ALL)
    @JoinColumn(name = "anon_cart_id")
    private Set<AnonCartItem> anonCartItems;

    public AnonCart(){
        this.cartUid = randomUUID();
        this.anonCartItems = new HashSet<AnonCartItem>();
    }

    public AnonCart(Set<AnonCartItem> anonCartItems){
        this.cartUid = randomUUID();
        this.anonCartItems = anonCartItems;
    }

    public UUID getCartUid() {
        return cartUid;
    }

    public Set<AnonCartItem> getAnonCartItems() {
        return anonCartItems;
    }

    public void setAnonCartItems(Set<AnonCartItem> anonCartItems) {
        this.anonCartItems = anonCartItems;
    }

    public long getId() {
        return id;
    }

    public void addAnonCartItem(AnonCartItem anonCartItem){
        this.anonCartItems.add(anonCartItem);
    }

    public int getTotalCount(){
        return anonCartItems.size();
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public double getTotalPrice(){
        double totalPrice = 0D;
        for(AnonCartItem anonCartItem : anonCartItems){
            totalPrice = totalPrice + anonCartItem.getProductPrice() * anonCartItem.getQuantity();
        }
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnonCart anonCart = (AnonCart) o;

        if (customerId != anonCart.customerId) return false;
        if (cartUid != null ? !cartUid.equals(anonCart.cartUid) : anonCart.cartUid != null) return false;
        return !(anonCartItems != null ? !anonCartItems.equals(anonCart.anonCartItems) : anonCart.anonCartItems != null);

    }

    @Override
    public int hashCode() {
        int result = cartUid != null ? cartUid.hashCode() : 0;
        result = 31 * result + (int) (customerId ^ (customerId >>> 32));
        result = 31 * result + (anonCartItems != null ? anonCartItems.hashCode() : 0);
        return result;
    }
}
