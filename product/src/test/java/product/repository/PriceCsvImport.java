package product.repository;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.*;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PriceCsvImport extends AbstractRepositoryTest {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private ProductRepository productRepository;


    @Test
    @Rollback(false)
    public void importData() throws Exception {
        String fileName = "src/test/resources/promotion_20210228.csv";
        Path myPath = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<PromotionData> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(PromotionData.class);

            CsvToBean<PromotionData> csvToBean = new CsvToBeanBuilder<PromotionData>(br)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<PromotionData> priceDataList = csvToBean.parse();
            for (PromotionData priceData : priceDataList) {
                Product product = productRepository.findByCode(priceData.getProductCode()).get();
                Sku sku = product.getSkus().stream().filter(item -> item.getSku().equals(priceData.getSku())).findFirst().get();
                Price price = Price.builder()
                        .price(BigDecimal.valueOf(Double.valueOf(priceData.getPromotionPrice())))
                        .startDate(LocalDate.parse(priceData.getStartDate(), formatter))
                        .endDate(LocalDate.parse(priceData.getEndDate(), formatter))
                        .discountRate(priceData.getPromotionRate())
                        .build();
                sku.addPrice(price);
                ProductTag productTag = ProductTag.builder()
                        .tag("限時搶購")
                        .startDate(LocalDate.parse(priceData.getStartDate(), formatter))
                        .endDate(LocalDate.parse(priceData.getEndDate(), formatter))
                        .colorHex("#FF0000")
                        .build();
                product.addTag(productTag);
                productRepository.save(product);

            }

        }
    }

    @Data
    public static class PromotionData {
        @CsvBindByName(column = "product_code")
        private String productCode;
        @CsvBindByName(column = "sku")
        private String sku;
        @CsvBindByName(column = "promotion_price")
        private String promotionPrice;
        @CsvBindByName(column = "start_date")
        private String startDate;
        @CsvBindByName(column = "end_date")
        private String endDate;
        @CsvBindByName(column = "promotion_rate")
        private String promotionRate;
    }

}
