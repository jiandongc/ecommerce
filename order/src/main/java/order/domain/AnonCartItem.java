package order.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "anon_cart_item")
public class AnonCartItem {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "anon_cart_item_seq", sequenceName = "anon_cart_item_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anon_cart_item_seq")
    private long id;
    @Column(name = "product_id")
    private long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_price")
    private double productPrice;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "anon_cart_id")
    private AnonCart anonCart;

    public AnonCartItem() {
    }

    public AnonCartItem(long productId, String productName, double productPrice, int quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public AnonCart getAnonCart() {
        return anonCart;
    }

    public void setAnonCart(AnonCart anonCart) {
        if (this.anonCart != null) {
            this.anonCart.internalRemoveAnonCartItem(this);
        }
        this.anonCart = anonCart;
        if (anonCart != null) {
            anonCart.internalAddAnonCartItem(this);
        }
    }

    public double getSubTotal() {
        return productPrice * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnonCartItem that = (AnonCartItem) o;

        if (productId != that.productId) return false;
        if (Double.compare(that.productPrice, productPrice) != 0) return false;
        if (quantity != that.quantity) return false;
        return !(productName != null ? !productName.equals(that.productName) : that.productName != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (productId ^ (productId >>> 32));
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        temp = Double.doubleToLongBits(productPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantity;
        return result;
    }
}
