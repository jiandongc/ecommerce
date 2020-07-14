package product.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import product.domain.Category;
import product.domain.Product;

public interface ProductService {
	List<Product> findByCategoryCode(String categoryCode);
	Map<Category, Integer> findProductTotalInSubCategories(String categoryCode);
	Optional<Product> findByCode(String code);
	List<Product> findRelatedProducts(String type, String code);
	List<Product> findProducts(String categoryCode, List<String> tags, String brand, String sort);
}
