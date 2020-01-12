package product.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_tag")
public class ProductTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "tag")
    private String tag;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "color_hex")
    private String colorHex;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Map<String, String> getAsMap(){
        Map<String, String> map = new HashMap<>();
        map.put("tag", this.tag);
        map.put("colorHex", this.colorHex);
        return map;
    }
}
