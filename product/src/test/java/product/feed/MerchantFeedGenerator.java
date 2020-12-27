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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MerchantFeedGenerator extends AbstractRepositoryTest {

    private static final String BASE_URL = "https://noodle-monster.co.uk";
    private static final String BASE_DIRECTORY = "/Users/jiandongchen/Projects";

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
                        .description(product.getShortDescription())
                        .link(BASE_URL + "/products/" + product.getCode())
                        .price(product.getOriginalPrice() + " GBP")
                        .salePrice(product.getSkus().get(0).getSalePrice() != null ? product.getSkus().get(0).getSalePrice().getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + " GBP":  null)
                        .salePriceEffectiveDate(product.getSkus().get(0).getSalePrice() != null ? product.getSkus().get(0).getSalePrice().getStartDate() + "/" + product.getSkus().get(0).getSalePrice().getEndDate() : null)
                        .availability(product.getSkus().get(0).getStockQuantity() > 0 ? "in stock" : "out of stock")
                        .imageLink(product.getFirstImageUrl())
                        .gtin(null)
                        .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                        .googleProductCategory(getCategoryNumber(product.getCode()))
                        .shipping(null)
                        .expirationDate(product.getEndDate() != null ? product.getEndDate().toString() : null)
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
        @CsvBindByPosition(position = 0)
        private String id;
        @CsvBindByName(column = "title")
        @CsvBindByPosition(position = 1)
        private String title;
        @CsvBindByName(column = "description")
        @CsvBindByPosition(position = 2)
        private String description;
        @CsvBindByName(column = "link")
        @CsvBindByPosition(position = 3)
        private String link;
        @CsvBindByName(column = "price")
        @CsvBindByPosition(position = 4)
        private String price;
        @CsvBindByName(column = "sale_price")
        @CsvBindByPosition(position = 5)
        private String salePrice;
        @CsvBindByName(column = "sale_price_effective_date")
        @CsvBindByPosition(position = 6)
        private String salePriceEffectiveDate;
        @CsvBindByName(column = "availability")
        @CsvBindByPosition(position = 7)
        private String availability;
        @CsvBindByName(column = "image link")
        @CsvBindByPosition(position = 8)
        private String imageLink;
        @CsvBindByName(column = "gtin")
        @CsvBindByPosition(position = 9)
        private String gtin;
        @CsvBindByName(column = "brand")
        @CsvBindByPosition(position = 10)
        private String brand;
        @CsvBindByName(column = "google product category")
        @CsvBindByPosition(position = 11)
        private String googleProductCategory;
        @CsvBindByName(column = "shipping")
        @CsvBindByPosition(position = 12)
        private String shipping;
        @CsvBindByName(column = "expiration_date")
        @CsvBindByPosition(position = 13)
        private String expirationDate;

    }

}


