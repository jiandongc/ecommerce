package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.domain.Product;

/**
 * Created by jiandong on 13/11/16.
 */

@Component
public class ProductDataMapper {

    public ProductData getValue(Product product) {
        return new ProductData(
//                product.getId(),
//                product.getName(),
//                product.getUnitPrice(),
//                product.getDescription(),
//                product.getCategory() != null ? product.getCategory().getName() : null,
//                product.getBrand() != null ? product.getBrand().getName() : null,
//                product.getImageUrl()
        );
    }
}
