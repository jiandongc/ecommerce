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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .filter(product -> product.getSkus().get(0).getStockQuantity() > 0)
                .filter(product -> product.isActive())
                .map(product -> ProductFeed.builder()
                        .id(product.getSkus().get(0).getSku())
                        .title(product.getName())
                        .description(product.getShortDescription())
                        .link(BASE_URL + "/products/" + product.getCode())
                        .price(product.getCurrentPrice() + " GBP")
                        .availability(product.getSkus().get(0).getStockQuantity() > 0 ? "in stock" : "out of stock")
                        .imageLink(product.getImages().get(0).getUrl())
                        .gtin(null)
                        .brand(product.getBrand() != null ? product.getBrand().getName() : null)
                        .googleProductCategory(getCategoryNumber(product.getCode()))
                        .shipping(null)
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
        @CsvBindByName(column = "availability")
        @CsvBindByPosition(position = 5)
        private String availability;
        @CsvBindByName(column = "image link")
        @CsvBindByPosition(position = 6)
        private String imageLink;
        @CsvBindByName(column = "gtin")
        @CsvBindByPosition(position = 7)
        private String gtin;
        @CsvBindByName(column = "brand")
        @CsvBindByPosition(position = 8)
        private String brand;
        @CsvBindByName(column = "google product category")
        @CsvBindByPosition(position = 9)
        private String googleProductCategory;
        @CsvBindByName(column = "shipping")
        @CsvBindByPosition(position = 10)
        private String shipping;
    }

}


