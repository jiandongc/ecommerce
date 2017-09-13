package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductSimpleData {

    private final String name;
    private final String description;
    private final String code;
    private final String imageUrl;

    @JsonCreator
    private ProductSimpleData(@JsonProperty("name") String name,
                              @JsonProperty("description") String description,
                              @JsonProperty("code") String code,
                              @JsonProperty("imageUrl") String imageUrl) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.imageUrl = imageUrl;
    }

    public static ProductSimpleDataBuilder builder(){
        return new ProductSimpleDataBuilder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSimpleData that = (ProductSimpleData) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        return !(imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }

    public static class ProductSimpleDataBuilder {
        private String name;
        private String description;
        private String code;
        private String imageUrl;

        public ProductSimpleDataBuilder name(String name){
            this.name = name;
            return this;
        }

        public ProductSimpleDataBuilder description(String description){
            this.description = description;
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

        public ProductSimpleData build(){
            return new ProductSimpleData(name, description, code, imageUrl);
        }
    }






}
