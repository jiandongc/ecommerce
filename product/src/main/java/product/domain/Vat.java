package product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vat_rate")
public class Vat {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Integer rate;
}
