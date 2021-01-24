package product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "country")
    private String country;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonIgnore
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "ordering")
    private Integer ordering;

    @JsonIgnore
    public Map<String, String> getAsMap() {
        final Map<String, String> values = new HashMap<>();
        values.put("name", this.name);
        values.put("code", this.code);
        return values;
    }
}
