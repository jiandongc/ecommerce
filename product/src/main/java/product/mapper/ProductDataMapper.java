package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.domain.Image;
import product.domain.Product;

import java.util.Comparator;


@Component
public class ProductDataMapper {

    public ProductData getValue(Product product) {
        final ProductData.ProductDataBuilder builder = ProductData.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .categoryCode(product.getCategoryCode())
                .price(product.getMinPrice());

        product.getSkus().forEach(sku -> {
            builder.addVariant(sku.getAsMap());
            sku.getAttributes().forEach(attribute -> builder.addAttribute(attribute.getKeyName(), attribute.getValue()));
        });

        product.getImages().stream()
                .sorted(Comparator.comparing(Image::getOrdering))
                .forEach(image -> builder.addImage(image.getImageTypeValue(), image.getUrl()));

        return builder.build();
    }
}
