package customer.domain;

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
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Token.Type type;

    @JsonIgnore
    @Column(name = "start_ts")
    private LocalDateTime startDateTime;

    @JsonIgnore
    @Column(name = "end_ts")
    private LocalDateTime endDateTime;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    public boolean isValid(){
        final LocalDateTime now = LocalDateTime.now();
        return (this.startDateTime != null && this.startDateTime.isBefore(now))
                && (this.endDateTime == null || this.endDateTime.isAfter(now));
    }

    public enum Type {
        EMAIL_RESET
    }

}
