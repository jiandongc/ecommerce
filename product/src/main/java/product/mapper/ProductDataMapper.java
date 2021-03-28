package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductData;
import product.domain.Product;
import product.domain.ProductTag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class ProductDataMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM. dd");

    public ProductData getValue(Product product) {
        final Map<String, Set<String>> attributes = new HashMap<>();
        final List<Map<String, Object>> variants = new ArrayList<>();
        product.getSkus().forEach(sku -> {
            Map<String, Object> skuMap = sku.getAsMap();
            skuMap.put("saleEndDate", formatDate(sku.getCurrentSaleEndDate()));
            variants.add(skuMap);
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
        if (validTags != null && !validTags.isEmpty()) {
            validTags.forEach(validTag -> tags.add(validTag.getAsMap()));
        }

        final List<String> images = new ArrayList<>();
        product.getImages().forEach(image -> images.add(image.getUrl()));

        return ProductData.builder()
                .type(product.getAttribute("Type"))
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .categoryCode(product.getCategoryCode())
                .vat(product.getVat() != null ? product.getVat().getRate() : 0)
                .price(product.getCurrentPrice())
                .originalPrice(product.getOriginalPrice())
                .discountRate(product.getDiscountRate())
                .isOnSale(product.isOnSale())
                .saleEndDate(formatDate(product.getSalesEndDate()))
                .attributes(attributes.isEmpty() ? null : attributes)
                .variants(variants.isEmpty() ? null : variants)
                .brand(product.getBrand() != null ? product.getBrand().getAsMap() : null)
                .tags(tags.isEmpty() ? null : tags)
                .images(images.isEmpty() ? null : images)
                .build();

    }

    private String formatDate(LocalDate salesEndDate) {
        if (salesEndDate == null || LocalDate.now().plusDays(7).isBefore(salesEndDate)) {
            return null;
        }

        String dayOfWeek = salesEndDate.getDayOfWeek().name();
        dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
        return dayOfWeek + ", " + dateTimeFormatter.format(salesEndDate);
    }
}
