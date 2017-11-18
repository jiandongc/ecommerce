package product.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacetValue {

    private String name;
    private int count;
    private boolean selected;

    @JsonCreator
    public FacetValue(@JsonProperty("name") String name,
                      @JsonProperty("count") int count,
                      @JsonProperty("selected") boolean selected){
        this.name = name;
        this.count = count;
        this.selected = selected;
    }

    public static FacetValueBuilder builder(){
        return new FacetValueBuilder();
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacetValue that = (FacetValue) o;

        if (count != that.count) return false;
        if (selected != that.selected) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + count;
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }

    public static class FacetValueBuilder {
        private String name;
        private int count;
        private boolean selected;

        public FacetValueBuilder name(String name){
            this.name = name;
            return this;
        }

        public FacetValueBuilder count(int count){
            this.count = count;
            return this;
        }

        public FacetValueBuilder isSelected(boolean selected){
            this.selected = selected;
            return this;
        }

        public FacetValue build(){
            return new FacetValue(name, count, selected);
        }
    }

}
