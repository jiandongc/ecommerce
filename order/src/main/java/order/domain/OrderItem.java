package order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_code")
    private String code;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_description")
    private String description;

    @Column(name = "unit_price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

}
