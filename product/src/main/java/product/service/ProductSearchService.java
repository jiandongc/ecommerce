package product.service;

import product.data.ProductSearchData;
import product.domain.Category;
import product.domain.Product;

import java.util.List;

public interface ProductSearchService {
    ProductSearchData filter(Category category, List<Product> products, String filterJsonStr, String sort);
}
