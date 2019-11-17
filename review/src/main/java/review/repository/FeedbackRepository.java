package review.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import review.domain.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    Feedback findBy_id(ObjectId _id);
}
