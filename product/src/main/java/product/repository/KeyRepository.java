package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Key;

/**
 * Created by jiandong on 28/08/17.
 */
public interface KeyRepository extends JpaRepository<Key, Long> {
}
