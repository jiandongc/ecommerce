package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.domain.Product;
import product.domain.ProductTag;

import java.util.*;


@Component
public class ProductDataMapper {

    public ProductData getValue(Product product) {
        final Map<String, Set<String>> attributes = new HashMap<>();
        final List<Map<String, Object>> variants = new ArrayList<>();
        product.getSkus().forEach(sku -> {
            variants.add(sku.getAsMap());
            sku.getAttributes().forEach(attribute -> {
                if (attributes.containsKey(attribute.getKey())) {
                    attributes.get(attribute.getKey()).add(attribute.getValue());
                } else {
                    final Set<String> values = new LinkedHashSet<>();
                    values.add(attribute.getValue());
                    attributes.put(attribute.getKey(), values);
                }
            });
        });

        final List<Map<String, String>> tags = new ArrayList<>();
        final List<ProductTag> validTags = product.getValidTags();
        if(validTags != null && !validTags.isEmpty()){
            validTags.forEach(validTag -> tags.add(validTag.getAsMap()));
        }

        final List<String> images = new ArrayList<>();
        product.getImages().forEach(image -> images.add(image.getUrl()));

        return ProductData.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .categoryCode(product.getCategoryCode())
                .vat(product.getVat() != null ? product.getVat().getRate() : 0)
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .attributes(attributes.isEmpty() ? null : attributes)
                .variants(variants.isEmpty() ? null : variants)
                .brand(product.getBrand() != null ? product.getBrand().getAsMap() : null)
                .tags(tags.isEmpty() ? null: tags)
                .images(images.isEmpty() ? null : images)
                .build();

    }
}
