package product.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "sku")
    private List<Price> prices;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "sku")
    private List<SkuAttribute> attributes;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public BigDecimal getCurrentPrice() {
        return getCurrentSalePrice() != null ? getCurrentSalePrice() : getOriginalPrice();
    }

    public BigDecimal getOriginalPrice() {
        if (prices == null) {
            return null;
        }

        final LocalDate today = LocalDate.now();
        final Optional<Price> priceOptional = prices.stream()
                .filter(price -> price.getStartDate() != null && !today.isBefore(price.getStartDate()) && price.getEndDate() == null)
                .findFirst();
        return priceOptional.map(price -> price.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP)).orElse(null);
    }

    public BigDecimal getCurrentSalePrice() {
        if (prices == null) {
            return null;
        }

        final LocalDate today = LocalDate.now();
        final Optional<Price> priceOptional = prices.stream()
                .filter(price -> price.getStartDate() != null && price.getEndDate() != null && !today.isBefore(price.getStartDate()) && !today.isAfter(price.getEndDate()))
                .findFirst();
        return priceOptional.map(price -> price.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP)).orElse(null);
    }

    public LocalDate getCurrentSaleEndDate() {
        Price price = getSalePrice();
        return price != null ? price.getEndDate() : null;
    }


    public Price getSalePrice() {
        if (prices == null) {
            return null;
        }

        final LocalDate today = LocalDate.now();
        return prices.stream()
                .filter(price -> price.getStartDate() != null && price.getEndDate() != null && !today.isBefore(price.getStartDate()) && !today.isAfter(price.getEndDate()))
                .findFirst().orElse(null);

    }

    public String getDiscountRate() {
        final LocalDate today = LocalDate.now();
        final Optional<Price> priceOptional = prices.stream()
                .filter(price -> price.getStartDate() != null && price.getEndDate() != null && !today.isBefore(price.getStartDate()) && !today.isAfter(price.getEndDate()))
                .findFirst();
        return priceOptional.map(Price::getDiscountRate).orElse(null);
    }

    public boolean isOnSale() {
        final LocalDate today = LocalDate.now();
        final Optional<Price> priceOptional = prices.stream()
                .filter(price -> price.getStartDate() != null && price.getEndDate() != null && !today.isBefore(price.getStartDate()) && !today.isAfter(price.getEndDate()))
                .findFirst();
        return priceOptional.isPresent();
    }

    public void addPrice(Price price) {
        if (prices == null) {
            prices = new ArrayList<>();
        }

        prices.add(price);
        price.setSku(this);
    }

    public void addAttribute(SkuAttribute skuAttribute) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        attributes.add(skuAttribute);
        skuAttribute.setSku(this);
    }

    public Map<String, Object> getAsMap() {
        final Map<String, Object> values = new HashMap<>();
        values.put("sku", sku);
        values.put("qty", stockQuantity);
        values.put("price", this.getCurrentPrice());
        values.put("originalPrice", this.getOriginalPrice());
        values.put("discountRate", this.getDiscountRate());
        values.put("isOnSale", this.isOnSale());
        attributes.forEach(attribute -> values.put(attribute.getKey(), attribute.getValue()));
        values.put("description", attributes.stream()
                .map(attribute -> attribute.getKey() + ": " + attribute.getValue())
                .collect(Collectors.joining(", "))
        );
        return values;
    }
}
