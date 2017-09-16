package product.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import product.data.ProductSimpleData;
import product.domain.Product;
import product.mapper.ProductDataMapper;
import product.mapper.ProductSimpleDataMapper;
import product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	private final ProductSimpleDataMapper simpleProductMapper;
	private final ProductDataMapper productMapper;
	
	@Autowired
	public ProductController(ProductService productService,
							 ProductSimpleDataMapper simpleProductMapper,
							 ProductDataMapper productMapper){
		this.productService = productService;
		this.simpleProductMapper = simpleProductMapper;
		this.productMapper = productMapper;
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<ProductSimpleData> findProducts(@RequestParam(value = "cc", required = true) String categoryCode) {
		return productService.findByCategoryCode(categoryCode).stream()
				.map(simpleProductMapper::getValue).collect(Collectors.toList());
	}
	
    @RequestMapping(value = "/{code}", method=RequestMethod.GET)
    public ResponseEntity findByCode(@PathVariable String code) {
		final Optional<Product> product = productService.findByCode(code);

		if(product.isPresent()){
			return new ResponseEntity<>(productMapper.getValue(product.get()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
    
}
