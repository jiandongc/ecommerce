package product.domain;

import org.w3c.dom.Attr;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by jiandong on 19/08/17.
 */
@Entity
@Table(name = "sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "sku")
    private String sku;
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    @Column(name = "price")
    private BigDecimal price;
    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "sku_attribute_value",
            joinColumns = {@JoinColumn(name = "sku_id")},
            inverseJoinColumns = {@JoinColumn(name = "attribute_value_id")})
    private List<Attribute> attributes = new ArrayList<Attribute>();
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute){
        attributes.add(attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sku sku1 = (Sku) o;

        if (sku != null ? !sku.equals(sku1.sku) : sku1.sku != null) return false;
        if (stockQuantity != null ? !stockQuantity.equals(sku1.stockQuantity) : sku1.stockQuantity != null)
            return false;
        if (price != null ? !price.equals(sku1.price) : sku1.price != null) return false;
        return !(attributes != null ? !attributes.equals(sku1.attributes) : sku1.attributes != null);

    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (stockQuantity != null ? stockQuantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}
