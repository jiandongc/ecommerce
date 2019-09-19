package product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_code")
    private String code;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Product parent;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
    @OrderBy(value = "ordering")
    private List<Image> images;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
    private List<Sku> skus;

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "product_attribute_value", joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {@JoinColumn(name = "attribute_value_id")})
    private List<Attribute> attributes;

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addImage(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }

        images.add(image);
        image.setProduct(this);
    }

    public void addSku(Sku sku) {
        if (skus == null) {
            skus = new ArrayList<>();
        }

        skus.add(sku);
        sku.setProduct(this);
    }

    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        attributes.add(attribute);
    }

    public String getFirstImageUrl() {
        if(images != null && !images.isEmpty()){
            return images.get(0).getUrl();
        } else {
            return null;
        }
    }

    public BigDecimal getCurrentPrice() {
        if(skus == null){
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getCurrentPrice).orElse(null);
    }

    public BigDecimal getOriginalPrice() {
        if(skus == null){
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getOriginalPrice).orElse(null);
    }

    public String getDiscountRate(){
        if(skus == null){
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getDiscountRate).orElse(null);
    }

    public boolean isOnSale(){
        if(skus == null){
            return false;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::isOnSale).orElse(false);
    }

    public String getCategoryCode() {
        return category.getCode();
    }

}
