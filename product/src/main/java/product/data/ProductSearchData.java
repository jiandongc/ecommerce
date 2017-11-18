package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchData {
    private List<ProductSimpleData> products;
    private List<Facet> facets;

    @JsonCreator
    public ProductSearchData(@JsonProperty("products") List<ProductSimpleData> products,
                             @JsonProperty("facets") List<Facet> facets) {
        this.products = products;
        this.facets = facets;
    }

    public static ProductSearchDataBuilder builder(){
        return new ProductSearchDataBuilder();
    }

    public List<ProductSimpleData> getProducts() {
        return products;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSearchData that = (ProductSearchData) o;

        if (products != null ? !products.equals(that.products) : that.products != null) return false;
        return facets != null ? facets.equals(that.facets) : that.facets == null;
    }

    @Override
    public int hashCode() {
        int result = products != null ? products.hashCode() : 0;
        result = 31 * result + (facets != null ? facets.hashCode() : 0);
        return result;
    }

    public static class ProductSearchDataBuilder {
        private List<ProductSimpleData> products = new ArrayList<>();
        private List<Facet> facets = new ArrayList<>();

        public ProductSearchDataBuilder addProduct(ProductSimpleData productSimpleData){
            this.products.add(productSimpleData);
            return this;
        }

        public ProductSearchDataBuilder addFacet(Facet facet){
            this.facets.add(facet);
            return this;
        }

        public ProductSearchDataBuilder products(List<ProductSimpleData> products){
            this.products = products;
            return this;
        }

        public ProductSearchData build(){
            return new ProductSearchData(products, facets);
        }
    }
}
