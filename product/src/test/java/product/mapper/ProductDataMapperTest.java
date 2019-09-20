package product.mapper;

import org.junit.Test;
import product.data.ProductData;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

        final Key color = new Key();
        color.setName("Color");
        final Key size = new Key();
        size.setName("Size");

        final Sku sku1 = new Sku();
        sku1.addPrice(Price.builder().price(TEN).startDate(LocalDate.now()).build());
        sku1.setStockQuantity(100);
        sku1.setSku("FD10039403_X");
        final Attribute attribute1 = new Attribute();
        attribute1.setKey(color);
        attribute1.setValue("Red");
        sku1.addAttribute(attribute1);
        final Attribute attribute2 = new Attribute();
        attribute2.setKey(size);
        attribute2.setValue("XL");
        sku1.addAttribute(attribute2);
        product.addSku(sku1);

        final Sku sku2 = new Sku();
        sku2.addPrice(Price.builder().price(BigDecimal.valueOf(1.5)).startDate(LocalDate.now()).build());
        sku2.setStockQuantity(99);
        sku2.setSku("FD10039403_Y");
        final Attribute attribute3 = new Attribute();
        attribute3.setKey(color);
        attribute3.setValue("Blue");
        sku2.addAttribute(attribute3);
        final Attribute attribute4 = new Attribute();
        attribute4.setKey(size);
        attribute4.setValue("XXL");
        sku2.addAttribute(attribute4);
        product.addSku(sku2);

        // When
        final ProductData actual = mapper.getValue(product);

        // Then
        final Map<String, Set<String>> attributes = new LinkedHashMap<>();
        attributes.put("Size", new LinkedHashSet<>(Arrays.asList("XL", "XXL")));
        attributes.put("Color", new LinkedHashSet<>(Arrays.asList("Red", "Blue")));
        final Map<String, Object> variantOne = new HashMap<>();
        variantOne.put("sku", "FD10039403_X");
        variantOne.put("qty", 100);
        variantOne.put("price", TEN);
        variantOne.put("originalPrice", TEN);
        variantOne.put("discountRate", null);
        variantOne.put("isOnSale", false);
        variantOne.put("Color", "Red");
        variantOne.put("Size", "XL");
        variantOne.put("description", "Color: Red, Size: XL");
        final Map<String, Object> variantTwo = new HashMap<>();
        variantTwo.put("sku", "FD10039403_Y");
        variantTwo.put("qty", 99);
        variantTwo.put("price", BigDecimal.valueOf(1.5));
        variantTwo.put("originalPrice", BigDecimal.valueOf(1.5));
        variantTwo.put("discountRate", null);
        variantTwo.put("isOnSale", false);
        variantTwo.put("Color", "Blue");
        variantTwo.put("Size", "XXL");
        variantTwo.put("description", "Color: Blue, Size: XXL");
        final List<Map<String, Object>> variants = Arrays.asList(variantOne, variantTwo);
        final List<String> images = Arrays.asList("url one", "url two", "url three");
        final ProductData expected = new ProductData("code", "name", "description", "FH", BigDecimal.valueOf(1.5), BigDecimal.valueOf(1.5), null, false, attributes, variants, images);
        assertThat(actual, is(expected));
    }

}