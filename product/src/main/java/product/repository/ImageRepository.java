package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Image;

import java.util.List;

/**
 * Created by jiandong on 22/08/17.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUrl(String url);
}
