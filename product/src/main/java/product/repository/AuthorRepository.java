package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.post.Author;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findBySlug(String slug);
}
