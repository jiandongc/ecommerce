package product.service;

import java.util.List;

import product.domain.Product;

public interface ProductService {
	Product findById(long Id);
	List<Product> findAll();
	List<Product> findByCategoryId(long categoryId);
}
