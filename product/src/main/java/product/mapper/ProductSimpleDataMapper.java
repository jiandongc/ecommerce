package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductSimpleData;
import product.domain.Product;

@Component
public class ProductSimpleDataMapper {

    public ProductSimpleData map(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getFirstImageUrl())
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .build();
    }

}
