package customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_WRITE)
    @Column(name = "address_uid")
    private UUID addressUid;

    @Column(name = "title")
    private String title;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "post_code")
    private String postcode;

    @Column(name = "default_address")
    private boolean defaultAddress;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
