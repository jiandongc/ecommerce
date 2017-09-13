package product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import product.data.ProductSimpleData;
import product.domain.Product;
import product.mapper.ProductSimpleDataMapper;
import product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	private final ProductSimpleDataMapper simpleProductMapper;
	
	@Autowired
	public ProductController(ProductService productService, ProductSimpleDataMapper simpleProductMapper){
		this.productService = productService;
		this.simpleProductMapper = simpleProductMapper;
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<ProductSimpleData> findProducts(@RequestParam(value = "cc", required = true) String categoryCode) {
		return productService.findByCategoryCode(categoryCode).stream()
				.map(simpleProductMapper::getValue).collect(Collectors.toList());
	}
	
    @RequestMapping(value = "/{code}", method=RequestMethod.GET)
    public Product findById(@PathVariable String code) {
    	return null; //productService.findById(id);
    }
    
}
