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
import java.util.List;

public class DataCsvImport extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private VatRepository vatRepository;

    @Test
    @Rollback(false)
    public void importData() throws Exception {
        String fileName = "src/test/resources/product_import_20200913.csv";
        Path myPath = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<ProductData> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ProductData.class);

            CsvToBean<ProductData> csvToBean = new CsvToBeanBuilder<ProductData>(br)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<ProductData> products = csvToBean.parse();

            for (ProductData productData : products) {
                Category category = categoryRepository.findByCode(productData.getCategoryCode()).get();
                Brand brand = brandRepository.findByCode(productData.getBrandCode()).get();
                Vat vat = vatRepository.findByName(productData.getVat()).get();

                Product product = Product.builder()
                        .category(category)
                        .brand(brand)
                        .code(productData.getProductCode())
                        .name(productData.getName())
                        .vat(vat)
                        .startDate(LocalDate.now())
                        .build();

                Sku sku = Sku.builder().sku(productData.getSku()).stockQuantity(Integer.valueOf(productData.getQuantity())).build();
                sku.addPrice(Price.builder().startDate(LocalDate.now()).price(BigDecimal.valueOf(Double.valueOf(productData.getPrice()))).build());
                product.addSku(sku);
                product.addImage(Image.builder().url(productData.getImage()).ordering(0).build());
                productRepository.save(product);
            }

        }
    }

    @Data
    public static class ProductData {

        @CsvBindByName(column = "category_code")
        private String categoryCode;
        @CsvBindByName(column = "brand_code")
        private String brandCode;
        @CsvBindByName(column = "product_code")
        private String productCode;
        @CsvBindByName(column = "name")
        private String name;
        @CsvBindByName(column = "vat_rate")
        private String vat;
        @CsvBindByName(column = "image")
        private String image;
        @CsvBindByName(column = "sku")
        private String sku;
        @CsvBindByName(column = "quantity")
        private String quantity;
        @CsvBindByName(column = "price")
        private String price;
    }

}
