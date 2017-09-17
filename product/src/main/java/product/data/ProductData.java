package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductData {

    private final String code;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Map<String, List<String>> attributes;
    private final List<Map<String, String>> variants;
    private final Map<String, String> images;

    @JsonCreator
    public ProductData(@JsonProperty("code") String code,
                       @JsonProperty("name") String name,
                       @JsonProperty("description") String description,
                       @JsonProperty("price") BigDecimal price,
                       @JsonProperty("attributes") Map<String, List<String>> attributes,
                       @JsonProperty("variants") List<Map<String, String>> variants,
                       @JsonProperty("images") Map<String, String> images) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.attributes = attributes;
        this.variants = variants;
        this.images = images;
    }

    public static ProductDataBuilder builder(){
        return new ProductDataBuilder();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public List<Map<String, String>> getVariants() {
        return variants;
    }

    public Map<String, String> getImages() {
        return images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductData that = (ProductData) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        if (variants != null ? !variants.equals(that.variants) : that.variants != null) return false;
        return !(images != null ? !images.equals(that.images) : that.images != null);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (variants != null ? variants.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }

    public static class ProductDataBuilder {
        private String code;
        private String name;
        private String description;
        private BigDecimal price;
        private Map<String, List<String>> attributes = new HashMap<>();
        private List<Map<String, String>> variants = new ArrayList<>();
        private Map<String, String> images = new HashMap<>();

        public ProductDataBuilder code(String code){
            this.code = code;
            return this;
        }

        public ProductDataBuilder name(String name){
            this.name = name;
            return this;
        }

        public ProductDataBuilder description(String description){
            this.description = description;
            return this;
        }

        public ProductDataBuilder price(BigDecimal price){
            this.price = price;
            return this;
        }

        public ProductDataBuilder addAttribute(String key, String value){
            if(attributes.containsKey(key)){
                attributes.get(key).add(value);
            } else {
                final List<String> values = new ArrayList<>();
                values.add(value);
                attributes.put(key, values);
            }

            return this;
        }

        public ProductDataBuilder addVariant(Map<String, String> variant){
            variants.add(variant);
            return this;
        }

        public ProductDataBuilder addImage(String key, String value){
            images.put(key, value);
            return this;
        }

        public ProductData build(){
            return new ProductData(code, name, description, price, attributes, variants, images);
        }
    }
}
