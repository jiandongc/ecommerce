package review.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import review.domain.Comment;
import review.repository.CommentRepository;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            return Optional.empty();
        } else {
            Hibernate.initialize(comment.getResponses());
        }

        return Optional.of(comment);
    }
}
