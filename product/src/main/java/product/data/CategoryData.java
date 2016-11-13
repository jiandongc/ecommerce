package product.data;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategoryData {

    private long id;
    private String name;
    private int productCount;

    public CategoryData(){}

    public CategoryData(long id, String name, int productCount) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
    }

    public CategoryData(long id, String name) {
        this.id = id;
        this.name = name;
        this.productCount = 0;
    }

    public String getName() {
        return name;
    }

    public int getProductCount() {
        return productCount;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryData that = (CategoryData) o;

        if (id != that.id) return false;
        if (productCount != that.productCount) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + productCount;
        return result;
    }
}
