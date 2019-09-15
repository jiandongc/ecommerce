package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductSimpleData;
import product.domain.Product;

@Component
public class ProductSimpleDataMapper {

    public ProductSimpleData getValueWithMainImage(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getMainImageUrl())
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .build();
    }

    public ProductSimpleData getValueWithColorImage(Product product) {
        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getColorImageUrl())
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .build();
    }
}
