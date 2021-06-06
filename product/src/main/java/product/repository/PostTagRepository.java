package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.post.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
