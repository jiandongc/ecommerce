package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.domain.Image;
import product.domain.Product;

import java.util.*;


@Component
public class ProductDataMapper {

    public ProductData getValue(Product product) {
        Map<String, Set<String>> attributes = new HashMap<>();
        List<Map<String, String>> variants = new ArrayList<>();
        Map<String, List<String>> images = new HashMap<>();

        product.getSkus().forEach(sku -> {
            variants.add(sku.getAsMap());
            sku.getAttributes().forEach(attribute -> {
                if (attributes.containsKey(attribute.getKeyName())) {
                    attributes.get(attribute.getKeyName()).add(attribute.getValue());
                } else {
                    final Set<String> values = new LinkedHashSet<>();
                    values.add(attribute.getValue());
                    attributes.put(attribute.getKeyName(), values);
                }
            });
        });

        product.getImages().stream()
                .sorted(Comparator.comparing(Image::getOrdering))
                .forEach(image -> {
                    if (images.containsKey(image.getImageTypeValue())) {
                        images.get(image.getImageTypeValue()).add(image.getUrl());
                    } else {
                        final List<String> values = new ArrayList<>();
                        values.add(image.getUrl());
                        images.put(image.getImageTypeValue(), values);
                    }
                });


        return ProductData.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .categoryCode(product.getCategoryCode())
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .attributes(product.getSkus().isEmpty() ? null : attributes)
                .variants(product.getSkus().isEmpty() ? null : variants)
                .images(product.getImages().isEmpty() ? null : images)
                .build();

    }
}
