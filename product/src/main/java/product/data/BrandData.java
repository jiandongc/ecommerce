package product.data;

/**
 * Created by jiandong on 13/11/16.
 */
public class BrandData {

    private long id;
    private String name;

    public BrandData(){}

    public BrandData(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrandData brandData = (BrandData) o;

        if (id != brandData.id) return false;
        return !(name != null ? !name.equals(brandData.name) : brandData.name != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
