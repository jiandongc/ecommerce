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

    public ProductSimpleData getValueWithMainImage(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getMainImageUrl())
                .price(product.getMinPrice())
                .build();
    }

    public ProductSimpleData getValueWithColorImage(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getColorImageUrl())
                .price(product.getMinPrice())
                .build();
    }
}
