package product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import product.domain.Product;
import product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService){
		this.productService = productService;
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<Product> findProducts(@RequestParam(value = "categoryId", required = false) Long categoryId) {
		if(categoryId != null){
			return productService.findByCategoryId(categoryId);
		}
		return productService.findAll();
	}
	
    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public Product findById(@PathVariable long id) {
    	return productService.findById(id);
    }
    
}
