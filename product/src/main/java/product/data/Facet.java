package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Facet {

    private String name;
    private boolean hasSelectedValue;
    private List<FacetValue> facetValues;

    @JsonCreator
    public Facet(@JsonProperty("name") String name,
                 @JsonProperty("hasSelectedValue") boolean hasSelectedValue,
                 @JsonProperty("facetValues") List<FacetValue> facetValues) {
        this.name = name;
        this.hasSelectedValue = hasSelectedValue;
        this.facetValues = facetValues;
    }

    public static FacetBuilder builder(){
        return new FacetBuilder();
    }

    public String getName() {
        return name;
    }

    public boolean isHasSelectedValue() {
        return hasSelectedValue;
    }

    public List<FacetValue> getFacetValues() {
        return facetValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Facet facet = (Facet) o;

        if (hasSelectedValue != facet.hasSelectedValue) return false;
        if (name != null ? !name.equals(facet.name) : facet.name != null) return false;
        return facetValues != null ? facetValues.equals(facet.facetValues) : facet.facetValues == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (hasSelectedValue ? 1 : 0);
        result = 31 * result + (facetValues != null ? facetValues.hashCode() : 0);
        return result;
    }

    public static class FacetBuilder {
        private String name;
        private boolean hasSelectedValue;
        private List<FacetValue> facetValues = new ArrayList<>();

        public FacetBuilder name(String name){
            this.name = name;
            return this;
        }

        public FacetBuilder hasSelectedValue(boolean hasSelectedValue){
            this.hasSelectedValue = hasSelectedValue;
            return this;
        }

        public FacetBuilder addFacetValue(FacetValue facetValue){
            this.facetValues.add(facetValue);
            return this;
        }

        public Facet build(){
            return new Facet(name, hasSelectedValue, facetValues);
        }
    }
}
