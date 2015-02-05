package product.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import product.domain.Product;
import product.service.ProductSerivce;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private ProductSerivce productService;
	
	@Autowired
	public ProductController(ProductSerivce productService){
		this.productService = productService;
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<Product> findAll(HttpServletResponse response) {
		return productService.findAll();
	}
	
    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public Product findById(@PathVariable long id) {
    	Product product = productService.findById(id);
        return product;
    }
    
}
