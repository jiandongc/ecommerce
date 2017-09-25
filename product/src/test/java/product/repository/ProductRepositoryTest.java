package product.repository;

import static java.math.BigDecimal.TEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import product.domain.*;

public class ProductRepositoryTest extends AbstractRepositoryTest{

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ImageTypeRepository imageTypeRepository;

	@Autowired
	private KeyRepository keyRepository;

	@Autowired
	private AttributeRepository attributeRepository;

	private Category category;
	private Brand brand;
	private ImageType imageType;
	private Image image;
	private Product parentProduct;
	private Key key;
	private Sku sku;
	private Attribute attribute;

	@Before
	public void setUp(){
		category = new Category();
		category.setHidden(false);
		category.setName("food");
		category.setDescription("delicious");
		category.setImageUrl("img/0001.jpg");
		category.setCode("FD");
		categoryRepository.save(category);

		brand = new Brand();
		brand.setName("Walkers");
		brandRepository.save(brand);

		imageType = new ImageType();
		imageType.setType("thumbnail");
		imageTypeRepository.save(imageType);

		image = new Image();
		image.setImageType(imageType);
		image.setUrl("img/0002.jpg");
		image.setOrdering(1);

		key = new Key();
		key.setName("Color");
		keyRepository.save(key);

		attribute = new Attribute();
		attribute.setKey(key);
		attribute.setValue("Red");
		attributeRepository.save(attribute);

		sku = new Sku();
		sku.setPrice(TEN);
		sku.setStockQuantity(100);
		sku.setSku("FD10039403_X");
		sku.addAttribute(attribute);
	}

	@Test
	public void shouldSaveAndFindItByProductCode(){
		// Given
		parentProduct = new Product();
		parentProduct.setName("parent");
		parentProduct.setCategory(category);
		productRepository.save(parentProduct);

		final Product product = new Product();
		product.setName("Chester");
		product.setDescription("delicious");
		product.setCategory(category);
		product.setBrand(brand);
		product.addImage(image);
		product.setParent(parentProduct);
		product.addSku(sku);
		productRepository.save(product);

		// When
		final String productCode = category.getCode() + String.format("%07d", product.getId());
		final Optional<Product> actualProduct = productRepository.findByCode(productCode);

		// Then
		assertThat(actualProduct.get(), is(product));
	}

	@Test
	public void shouldFindProductsByCategoryCode(){
		// Given
		final Product productOne = new Product();
		productOne.setName("Chester");
		productOne.setDescription("delicious");
		productOne.setCategory(category);
		productRepository.save(productOne);

		final Product productTwo = new Product();
		productTwo.setName("Coke");
		productTwo.setDescription("yummy");
		productTwo.setCategory(category);
		productRepository.save(productTwo);

		final Product productThree = new Product();
		productThree.setName("Cake");
		productThree.setDescription("getting fat");
		productThree.setCategory(category);
		productRepository.save(productThree);

		// When
		final List<Product> products = productRepository.findByCategoryCode(category.getCode());

		// Then
		assertThat(products.size(), is(3));
		assertThat(products, hasItems(productOne, productTwo, productThree));
	}
}
