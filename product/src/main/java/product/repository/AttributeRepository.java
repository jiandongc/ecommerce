package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Attribute;

/**
 * Created by jiandong on 28/08/17.
 */
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
