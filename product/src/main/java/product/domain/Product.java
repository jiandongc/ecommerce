package product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "product_code")
    private String code;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "vat_rate")
    private Vat vat;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Product parent;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
    @OrderBy(value = "ordering")
    private List<Image> images;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
    private List<Sku> skus;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
    private List<ProductAttribute> attributes;

    @OneToMany(fetch = EAGER, cascade = ALL, mappedBy = "product")
    private List<ProductTag> tags;

    @JsonIgnore
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonIgnore
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "ordering")
    private Integer ordering;

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

    public void addAttribute(ProductAttribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        attributes.add(attribute);
        attribute.setProduct(this);
    }

    public void addTag(ProductTag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
        tag.setProduct(this);
    }

    public String getFirstImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).getUrl();
        } else {
            return null;
        }
    }

    public BigDecimal getCurrentPrice() {
        if (skus == null) {
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getCurrentPrice).orElse(null);
    }

    public BigDecimal getOriginalPrice() {
        if (skus == null) {
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getOriginalPrice).orElse(null);
    }

    public String getDiscountRate() {
        if (skus == null) {
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getDiscountRate).orElse(null);
    }

    public boolean isOnSale() {
        if (skus == null) {
            return false;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::isOnSale).orElse(false);
    }

    public LocalDate getSalesEndDate() {
        if (skus == null) {
            return null;
        }

        final Optional<Sku> skuOptional = skus.stream().filter(sku -> sku.getCurrentPrice() != null).min(Comparator.comparing(Sku::getCurrentPrice));
        return skuOptional.map(Sku::getCurrentSaleEndDate).orElse(null);
    }

    public List<ProductTag> getValidTags() {
        if (tags == null){
            return null;
        }

        final LocalDate today = LocalDate.now();
        return tags.stream().filter(tag ->  (tag.getStartDate() != null && !today.isBefore(tag.getStartDate()))
                && (tag.getEndDate() == null || !today.isAfter(tag.getEndDate())))
                .collect(Collectors.toList());
    }

    public String getCategoryCode() {
        return category.getCode();
    }

    public boolean hasTag(List<String> tags) {

        List<ProductTag> validTags = this.getValidTags();

        if(validTags == null){
            return false;
        }

        for (int i = 0; i < validTags.size(); i++) {
            for (String tag: tags) {
                if (tag.equalsIgnoreCase(validTags.get(i).getTag())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isActive(){
        final LocalDate today = LocalDate.now();
        return (this.getStartDate() != null && !today.isBefore(this.getStartDate()))
                && (this.getEndDate() == null || !today.isAfter(this.getEndDate()));
    }

    public String getAttribute(String key) {
        if (attributes == null) {
            return null;
        }

        return attributes.stream().filter(attribute -> attribute.getKey().equals(key))
                .findFirst().map(ProductAttribute::getValue).orElse(null);
    }

}
