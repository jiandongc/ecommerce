package product.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
	@RequestMapping(value = "/search/categories/{categoryCode}", method=RequestMethod.GET)
	public ResponseEntity findProductsInCategory(@PathVariable String categoryCode,
												 @RequestParam(value = "filter", required = false) String filterJsonStr,
												 @RequestParam(value = "sort", required = false) String sort) {

		final List<Product> products = productService.findByCategoryCode(categoryCode);
		final Optional<Category> categoryOptional = categoryService.findByCode(categoryCode);
		final Category category = categoryOptional.orElseThrow(() -> new RuntimeException(String.format("Invalid Category Code %s", categoryCode)));
		final ProductSearchData productSearchData = productSearchService.filter(category, products, filterJsonStr, sort);

		if(!productSearchData.getProducts().isEmpty()){
			return new ResponseEntity<>(productSearchData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
	@RequestMapping(method=RequestMethod.GET)
	public List<ProductSimpleData> findProducts(@RequestParam(value = "cc", required = false) String categoryCode,
												@RequestParam(value = "tg", required = false) List<String> tags,
												@RequestParam(value = "br", required = false) String brand,
												@RequestParam(value = "sort", required = false) String sort) {

		return productService.findProducts(categoryCode, tags, brand, sort)
				.stream().map(simpleProductMapper::map)
				.collect(Collectors.toList());
	}

	@PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
    @RequestMapping(value = "/{code}", method=RequestMethod.GET)
    public ResponseEntity findByCode(@PathVariable String code) {
		final Optional<Product> productOptional = productService.findByCode(code);
		return productOptional.map(product -> new ResponseEntity<>(productMapper.getValue(product), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

	@PreAuthorize("hasAnyRole('ROLE_GUEST', 'ROLE_USER')")
	@RequestMapping(value = "/{type}/{code}", method=RequestMethod.GET)
	public List<ProductSimpleData> findRelatedProducts(@PathVariable(value = "type") String type, @PathVariable String code){
		return productService.findRelatedProducts(type, code)
				.stream().map(simpleProductMapper::map)
				.collect(Collectors.toList());
	}
    
}
