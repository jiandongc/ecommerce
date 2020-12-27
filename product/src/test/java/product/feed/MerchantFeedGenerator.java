package product.feed;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.Builder;
import lombok.Data;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.Product;
import product.repository.AbstractRepositoryTest;
import product.repository.ProductRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class MerchantFeedGenerator extends AbstractRepositoryTest {

    private static final String BASE_URL = "https://noodle-monster.co.uk";
    private static final String BASE_DIRECTORY = "/home/jiandong/Project2015";

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Rollback(false)
    public void generateProductFeed() throws Exception {
        Writer writer = new FileWriter(new File(BASE_DIRECTORY + "/ecommerce/product/src/test/resources/google_merchant_feed.csv"));

        StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .build();

        List<Product> products = productRepository.findAll();

        List<ProductFeed> productFeeds = products.stream()
                .filter(product -> product.isActive())
                .map(product -> ProductFeed.builder()
                        .id(product.getSkus().get(0).getSku())
                        .title(product.getName())
                        .description(product.getName())
                        .link(BASE_URL + "/products/" + product.getCode())
                        .price(product.getSkus().get(0).getOriginalPrice() + " GBP")
                        .salePrice(product.getSkus().get(0).getSalePrice() != null ? product.getSkus().get(0).getSalePrice().getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + " GBP":  null)
                        .salePriceEffectiveDate(product.getSkus().get(0).getSalePrice() != null ? product.getSkus().get(0).getSalePrice().getStartDate().atStartOfDay().format(ISO_DATE_TIME) + "/" + product.getSkus().get(0).getSalePrice().getEndDate().atTime(23, 59, 59).format(ISO_DATE_TIME) : null)
                        .availability(product.getSkus().get(0).getStockQuantity() > 0 ? "in stock" : "out of stock")
                        .imageLink(product.getFirstImageUrl())
                        .gtin(null)
                        .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                        .googleProductCategory(getCategoryNumber(product.getCode()))
                        .shipping(null)
                        .expirationDate(product.getEndDate() != null ? product.getEndDate().atTime(23, 59, 59).format(ISO_DATE_TIME) : null)
                        .build())
                .collect(Collectors.toList());

        sbc.write(productFeeds);
        writer.close();
    }

    private String getCategoryNumber(String productCode) {
        if (productCode.contains("noodle")) {
            return "434";
        } else if (productCode.contains("tea")) {
            return "2073";
        } else if (productCode.contains("snack")) {
            return "423";
        } else {
            return null;
        }
    }

    @Data
    @Builder
    public static class ProductFeed {

        @CsvBindByName(column = "id")
        private String id;
        @CsvBindByName(column = "title")
        private String title;
        @CsvBindByName(column = "description")
        private String description;
        @CsvBindByName(column = "link")
        private String link;
        @CsvBindByName(column = "price")
        private String price;
        @CsvBindByName(column = "sale_price")
        private String salePrice;
        @CsvBindByName(column = "sale_price_effective_date")
        private String salePriceEffectiveDate;
        @CsvBindByName(column = "availability")
        private String availability;
        @CsvBindByName(column = "image link")
        private String imageLink;
        @CsvBindByName(column = "gtin")
        private String gtin;
        @CsvBindByName(column = "brand")
        private String brand;
        @CsvBindByName(column = "google product category")
        private String googleProductCategory;
        @CsvBindByName(column = "shipping")
        private String shipping;
        @CsvBindByName(column = "expiration_date")
        private String expirationDate;

    }

}


