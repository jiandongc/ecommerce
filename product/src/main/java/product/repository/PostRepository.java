package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.post.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findBySlug(String code);
}
