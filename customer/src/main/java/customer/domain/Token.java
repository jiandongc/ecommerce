package customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_WRITE)
    @Column(name = "token_uid")
    private UUID tokenUid;

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
        PASSWORD_RESET
    }

}
