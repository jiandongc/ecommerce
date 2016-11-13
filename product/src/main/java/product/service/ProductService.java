package product.service;

import java.util.List;
import java.util.Map;

import product.domain.Category;
import product.domain.Product;

public interface ProductService {
	Product findById(long Id);
	List<Product> findAll();
	List<Product> findByCategoryId(long categoryId);
	Map<Category, Integer> findProductTotalInSubCategories(Long categoryId);
}
