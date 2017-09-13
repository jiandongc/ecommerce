package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryData {

    private String code;
    private String name;
    private int productTotal;
    private List<CategoryData> parents;
    private List<CategoryData> children;

    @JsonCreator
    private CategoryData(@JsonProperty("code") String code,
                         @JsonProperty("name") String name,
                         @JsonProperty("productTotal") int productTotal,
                         @JsonProperty("parents") List<CategoryData> parents,
                         @JsonProperty("children") List<CategoryData> children){
        this.code = code;
        this.name = name;
        this.productTotal = productTotal;
        this.parents = parents;
        this.children = children;
    }

    public static CategoryDataBuilder builder(){
        return new CategoryDataBuilder();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getProductTotal() {
        return productTotal;
    }

    public List<CategoryData> getParents() {
        return parents;
    }

    public List<CategoryData> getChildren() {
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryData that = (CategoryData) o;

        if (productTotal != that.productTotal) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parents != null ? !parents.equals(that.parents) : that.parents != null) return false;
        return !(children != null ? !children.equals(that.children) : that.children != null);

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + productTotal;
        result = 31 * result + (parents != null ? parents.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    public static class CategoryDataBuilder {
        private String code;
        private String name;
        private int productTotal;
        private List<CategoryData> parents;
        private List<CategoryData> children;

        public CategoryDataBuilder code(String code){
            this.code = code;
            return this;
        }

        public CategoryDataBuilder name(String name){
            this.name = name;
            return this;
        }

        public CategoryDataBuilder productTotal(int productTotal){
            this.productTotal = productTotal;
            return this;
        }

        public CategoryDataBuilder parents(List<CategoryData> parents){
            this.parents = parents;
            return this;
        }

        public CategoryDataBuilder children(List<CategoryData> children){
            this.children = children;
            return this;
        }

        public CategoryData build(){
            return new CategoryData(code, name, productTotal, parents, children);
        }
    }
}
