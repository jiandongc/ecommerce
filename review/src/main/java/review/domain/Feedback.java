package review.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    private String text;

    private int vote;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate creationDate;

    private List<Answer> answers;

    public void addAnswer(Answer answer) {
        if (answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answer);
    }

    public void addVote(){
        this.vote++;
    }
}
