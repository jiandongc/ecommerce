package product.service;

import java.util.List;

import product.domain.Product;

public interface ProductSerivce {
	public Product findById(Long Id);	
	public List<Product> findAll();
}
