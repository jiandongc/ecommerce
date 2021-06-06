package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.post.PostCategory;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    Optional<PostCategory> findBySlug(String slug);

}
