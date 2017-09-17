package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductSimpleData {

    private final String name;
    private final String code;
    private final String imageUrl;
    private final BigDecimal price;

    @JsonCreator
    private ProductSimpleData(@JsonProperty("name") String name,
                              @JsonProperty("code") String code,
                              @JsonProperty("imageUrl") String imageUrl,
                              @JsonProperty("price") BigDecimal price) {
        this.name = name;
        this.code = code;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public static ProductSimpleDataBuilder builder(){
        return new ProductSimpleDataBuilder();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSimpleData that = (ProductSimpleData) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        return !(price != null ? !price.equals(that.price) : that.price != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    public static class ProductSimpleDataBuilder {
        private String name;
        private String code;
        private String imageUrl;
        private BigDecimal price;

        public ProductSimpleDataBuilder name(String name){
            this.name = name;
            return this;
        }

        public ProductSimpleDataBuilder code(String code){
            this.code = code;
            return this;
        }

        public ProductSimpleDataBuilder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductSimpleDataBuilder price(BigDecimal price){
            this.price = price;
            return this;
        }

        public ProductSimpleData build(){
            return new ProductSimpleData(name, code, imageUrl, price);
        }
    }
}
