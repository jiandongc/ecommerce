package product.mapper;

import org.junit.Test;
import product.data.ProductData;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductDataMapperTest {

    private ProductDataMapper mapper = new ProductDataMapper();

    @Test
    public void shouldMapProductToProductData(){
        // Given
        final Product product = new Product();
        product.setCode("code");
        product.setName("name");
        product.setDescription("description");
        product.setShortDescription("short_description");
        product.setBrand(Brand.builder().name("nike").code("abc").build());
        product.setVat(Vat.builder().rate(20).name("uk_standard").build());

        product.addAttribute(ProductAttribute.builder().key("Type").value("Combo").build());

        final Category category = new Category();
        category.setCode("FH");
        product.setCategory(category);

        final Image imageOne = new Image();
        imageOne.setUrl("url one");
        imageOne.setOrdering(1);
        product.addImage(imageOne);
        final Image imageTwo = new Image();
        imageTwo.setUrl("url two");
        imageTwo.setOrdering(2);
        product.addImage(imageTwo);
        final Image imageThree = new Image();
        imageThree.setUrl("url three");
        imageThree.setOrdering(3);
        product.addImage(imageThree);

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().price(TEN).startDate(LocalDate.now()).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        sku1.addAttribute(SkuAttribute.builder().key("Color").value("Red").build());
        sku1.addAttribute(SkuAttribute.builder().key("Size").value("XL").build());
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().price(BigDecimal.valueOf(1.5)).startDate(LocalDate.now()).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        sku2.addAttribute(SkuAttribute.builder().key("Color").value("Blue").build());
        sku2.addAttribute(SkuAttribute.builder().key("Size").value("XXL").build());
        product.addSku(sku2);

        product.addTag(ProductTag.builder().tag("sale").startDate(LocalDate.now()).colorHex("#F0C14B").build());
        product.addTag(ProductTag.builder().tag("popular").startDate(LocalDate.now().minusDays(1)).colorHex("#C1F04B").build());
        product.addTag(ProductTag.builder().tag("future").startDate(LocalDate.now().plusDays(1)).build());
        product.addTag(ProductTag.builder().tag("past").startDate(LocalDate.now().minusDays(5)).endDate(LocalDate.now().minusDays(1)).build());

        // When
        final ProductData actual = mapper.getValue(product);

        // Then
        final Map<String, Set<String>> attributes = new LinkedHashMap<>();
        attributes.put("Size", new LinkedHashSet<>(Arrays.asList("XL", "XXL")));
        attributes.put("Color", new LinkedHashSet<>(Arrays.asList("Red", "Blue")));
        final Map<String, Object> variantOne = new HashMap<>();
        variantOne.put("sku", "FD10039403_X");
        variantOne.put("qty", 100);
        variantOne.put("price", TEN.setScale(2, ROUND_HALF_UP));
        variantOne.put("originalPrice", TEN.setScale(2, ROUND_HALF_UP));
        variantOne.put("discountRate", null);
        variantOne.put("isOnSale", false);
        variantOne.put("Color", "Red");
        variantOne.put("Size", "XL");
        variantOne.put("description", "Color: Red, Size: XL");
        variantOne.put("saleEndDate", null);
        final Map<String, Object> variantTwo = new HashMap<>();
        variantTwo.put("sku", "FD10039403_Y");
        variantTwo.put("qty", 99);
        variantTwo.put("price", BigDecimal.valueOf(1.5).setScale(2, ROUND_HALF_UP));
        variantTwo.put("originalPrice", BigDecimal.valueOf(1.5).setScale(2, ROUND_HALF_UP));
        variantTwo.put("discountRate", null);
        variantTwo.put("isOnSale", false);
        variantTwo.put("Color", "Blue");
        variantTwo.put("Size", "XXL");
        variantTwo.put("description", "Color: Blue, Size: XXL");
        variantTwo.put("saleEndDate", null);
        final List<Map<String, Object>> variants = Arrays.asList(variantOne, variantTwo);
        final List<String> images = Arrays.asList("url one", "url two", "url three");
        final Map<String, String> brand = new HashMap<>();
        brand.put("name", "nike");
        brand.put("code", "abc");
        final List<Map<String, String>> tags = new ArrayList<>();
        final Map<String, String> saleTag = new HashMap<>();
        saleTag.put("tag", "sale");
        saleTag.put("colorHex", "#F0C14B");
        tags.add(saleTag);
        final Map<String, String> popularTag = new HashMap<>();
        popularTag.put("tag", "popular");
        popularTag.put("colorHex", "#C1F04B");
        tags.add(popularTag);
        final ProductData expected = new ProductData(
                "code",
                "Combo",
                "name",
                "description",
                "short_description",
                "FH",
                20,
                BigDecimal.valueOf(1.5).setScale(2, ROUND_HALF_UP),
                BigDecimal.valueOf(1.5).setScale(2, ROUND_HALF_UP),
                null,
                false,
                null,
                attributes,
                variants,
                images,
                brand,
                tags);
        assertThat(actual, is(expected));
    }

}