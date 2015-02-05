package product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import product.domain.Product;
import product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductSerivce{
	
	public ProductRepository productRepository;
	
	@Autowired
	public ProductServiceImpl (ProductRepository productRepository){
		this.productRepository = productRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Product findById(Long Id) {
		return productRepository.findOne(Id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll(){
		return productRepository.findAll();
	}
}
