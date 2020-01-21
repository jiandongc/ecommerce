package product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @Column(name = "tag")
    private String tag;

    @JsonIgnore
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonIgnore
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "color_hex")
    private String colorHex;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    public Map<String, String> getAsMap(){
        Map<String, String> map = new HashMap<>();
        map.put("tag", this.tag);
        map.put("colorHex", this.colorHex);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductTag that = (ProductTag) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
