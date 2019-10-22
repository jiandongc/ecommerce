package customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "customer")
    private List<Address> addresses;

    @JsonIgnore
    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "customer")
    private List<Product> products;

    public void addAddress(Address address) {
        if (addresses == null) {
            addresses = new ArrayList<>();
        }

        this.addresses.add(address);
        address.setCustomer(this);
    }

    public void addProduct(Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }

        this.products.add(product);
        product.setCustomer(this);
    }

    @JsonIgnore
    public List<Product> getValidProducts() {
        if (products == null){
            return null;
        }

        final LocalDate today = LocalDate.now();
        return products.stream().filter(product ->  (product.getStartDate() != null && !today.isBefore(product.getStartDate()))
                && (product.getEndDate() == null || !today.isAfter(product.getEndDate())))
                .collect(Collectors.toList());
    }
}
