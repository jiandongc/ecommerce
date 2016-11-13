package product.data;

/**
 * Created by jiandong on 13/11/16.
 */
public class ProductData {

    private long id;
    private String name;
    private double unitPrice;
    private String description;
    private String category;
    private String brand;
    private String imageUrl;

    public ProductData(){}

    public ProductData(long id, String name, double unitPrice, String description, String category, String brand, String imageUrl) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductData that = (ProductData) o;

        if (id != that.id) return false;
        if (Double.compare(that.unitPrice, unitPrice) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        return !(imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(unitPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
