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
        final ProductData.ProductDataBuilder builder = ProductData.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .categoryCode(product.getCategoryCode())
                .price(product.getMinPrice());

        product.getSkus().stream().forEach(sku -> {
            builder.addVariant(sku.getMap());
            sku.getAttributes().stream().forEach(attribute -> builder.addAttribute(attribute.getKeyName(), attribute.getValue()));
        });

        product.getImages().stream()
                .sorted((i1, i2) -> Integer.compare(i1.getOrdering(), i2.getOrdering()))
                .forEach(image -> builder.addImage(image.getImageTypeValue(), image.getUrl()));

        return builder.build();
    }
}
