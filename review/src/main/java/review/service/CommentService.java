package review.service;

import review.domain.Comment;

import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(long id);

}
