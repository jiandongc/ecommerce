package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.data.ProductSimpleData;
import product.domain.Product;

/**
 * Created by jiandong on 13/11/16.
 */

@Component
public class ProductSimpleDataMapper {

    public ProductSimpleData getValue(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getMainImageUrl())
                .build();
    }
}
