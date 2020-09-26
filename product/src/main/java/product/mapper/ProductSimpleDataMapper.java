package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductSimpleData;
import product.domain.Product;
import product.domain.ProductTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProductSimpleDataMapper {

    public ProductSimpleData map(Product product) {

        final List<Map<String, String>> tags = new ArrayList<>();
        final List<ProductTag> validTags = product.getValidTags();
        if(validTags != null && !validTags.isEmpty()){
            validTags.forEach(validTag -> tags.add(validTag.getAsMap()));
        }

        return ProductSimpleData.builder()
                .code(product.getCode())
                .name(product.getName())
                .imageUrl(product.getFirstImageUrl())
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .ordering(product.getOrdering())
                .tags(tags.isEmpty() ? null: tags)
                .build();
    }

}
