package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.ImageType;

/**
 * Created by jiandong on 22/08/17.
 */
public interface ImageTypeRepository extends JpaRepository<ImageType, Long> {
}
