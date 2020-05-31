package review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import review.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCode(String code);

}
