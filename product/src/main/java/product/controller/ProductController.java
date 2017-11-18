package product.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import product.data.ProductSearchData;
import product.data.ProductSimpleData;
import product.domain.Category;
import product.domain.Product;
import product.mapper.ProductDataMapper;
import product.mapper.ProductSimpleDataMapper;
import product.service.CategoryService;
import product.service.ProductSearchService;
import product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final ProductSearchService productSearchService;
	private final ProductSimpleDataMapper simpleProductMapper;
	private final ProductDataMapper productMapper;
	private final CategoryService categoryService;

	@Autowired
	public ProductController(ProductService productService,
							 ProductSearchService productSearchService,
							 ProductSimpleDataMapper simpleProductMapper,
							 ProductDataMapper productMapper,
							 CategoryService categoryService){
		this.productService = productService;
		this.productSearchService = productSearchService;
		this.simpleProductMapper = simpleProductMapper;
		this.productMapper = productMapper;
		this.categoryService = categoryService;
	}

	@RequestMapping(value = "/search/categories/{categoryCode}", method=RequestMethod.GET)
	public ResponseEntity findProductsInCategory(@PathVariable String categoryCode,
												 @RequestParam(value = "filter", required = false) String filterJsonStr) {

		final List<Product> products = productService.findByCategoryCode(categoryCode);
		final Optional<Category> categoryOptional = categoryService.findByCode(categoryCode);
		final Category category = categoryOptional.orElseThrow(() -> new RuntimeException(String.format("Invalid Category Code %s", categoryCode)));
		final ProductSearchData productSearchData = productSearchService.filter(category, products, filterJsonStr);

		if(!productSearchData.getProducts().isEmpty()){
			return new ResponseEntity<>(productSearchData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<ProductSimpleData> findProducts(@RequestParam(value = "cc") String categoryCode) {
		return productService.findByCategoryCode(categoryCode).stream()
				.map(simpleProductMapper::getValueWithMainImage).collect(Collectors.toList());
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

	@RequestMapping(value = "/color/{code}", method=RequestMethod.GET)
	public ResponseEntity findColorVariant(@PathVariable String code){
		final List<ProductSimpleData> products = productService.findColorVariant(code).stream()
				.map(simpleProductMapper::getValueWithColorImage).collect(Collectors.toList());

		if(!products.isEmpty()){
			return new ResponseEntity<>(products, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
    
}
