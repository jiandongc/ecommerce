package customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

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
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_WRITE)
    @Column(name = "product_uid")
    private UUID productUid;

    @Column(name = "product_code")
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Product.Type type;

    @JsonIgnore
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonIgnore
    @Column(name = "end_date")
    private LocalDate endDate;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public boolean hasSameProductCodeAndType(Product product){
        return this.productCode.equals(product.getProductCode())
                && this.type.equals(product.getType());
    }

    public enum Type {
        FAVOURITE,
        NOTIFY_IN_STOCK
    }

}
