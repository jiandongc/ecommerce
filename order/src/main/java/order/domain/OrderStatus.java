package order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "status")
    private String status;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
