package review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
@Where(clause = "active = true")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "code")
    private String code;

    @Column(name = "comment")
    private String comment;

    @Column(name = "vote")
    private int vote;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "comment")
    @Where(clause = "active = true")
    private List<Response> responses;

    @JsonIgnore
    @Column(name = "active")
    private boolean active;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    public void addResponse(Response response) {
        if (this.responses == null) {
            this.responses = new ArrayList<>();
        }

        this.responses.add(response);
        response.setComment(this);
    }

    public void voteUp(){
        this.vote = this.vote + 1;
    }


}
