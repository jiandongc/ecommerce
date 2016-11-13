package product.data;

import java.util.List;
import java.util.Set;

/**
 * Created by jiandong on 13/11/16.
 */
public class CategorySummaryData {

    private Set<ProductData> products;
    private Set<CategoryData> subCategories;
    private Set<BrandData> brands;
    private List<CategoryData> parentCategories;
    private int productCount;

    public CategorySummaryData(){}

    public CategorySummaryData(Set<ProductData> products,
                               Set<CategoryData> subCategories,
                               Set<BrandData> brands,
                               List<CategoryData> parentCategories,
                               int productCount) {
        this.products = products;
        this.subCategories = subCategories;
        this.brands = brands;
        this.parentCategories = parentCategories;
        this.productCount = productCount;
    }

    public String getCategoryName(){
        return parentCategories.get(parentCategories.size()-1).getName();
    }

    public Set<ProductData> getProducts() {
        return products;
    }

    public Set<CategoryData> getSubCategories() {
        return subCategories;
    }

    public Set<BrandData> getBrands() {
        return brands;
    }

    public List<CategoryData> getParentCategories() {
        return parentCategories;
    }

    public int getProductCount() {
        return productCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategorySummaryData that = (CategorySummaryData) o;

        if (productCount != that.productCount) return false;
        if (products != null ? !products.equals(that.products) : that.products != null) return false;
        if (subCategories != null ? !subCategories.equals(that.subCategories) : that.subCategories != null)
            return false;
        if (brands != null ? !brands.equals(that.brands) : that.brands != null) return false;
        return !(parentCategories != null ? !parentCategories.equals(that.parentCategories) : that.parentCategories != null);

    }

    @Override
    public int hashCode() {
        int result = products != null ? products.hashCode() : 0;
        result = 31 * result + (subCategories != null ? subCategories.hashCode() : 0);
        result = 31 * result + (brands != null ? brands.hashCode() : 0);
        result = 31 * result + (parentCategories != null ? parentCategories.hashCode() : 0);
        result = 31 * result + productCount;
        return result;
    }
}
