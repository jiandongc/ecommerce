package product.mapper;

import org.springframework.stereotype.Component;
import product.data.ProductSimpleData;
import product.domain.Product;
import product.domain.ProductTag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ProductSimpleDataMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM. dd");

    public ProductSimpleData map(Product product) {

        final List<Map<String, String>> tags = new ArrayList<>();
        final List<ProductTag> validTags = product.getValidTags();
        if (validTags != null && !validTags.isEmpty()) {
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
                .saleEndDate(formatDate(product.getSalesEndDate()))
                .tags(tags.isEmpty() ? null : tags)
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
