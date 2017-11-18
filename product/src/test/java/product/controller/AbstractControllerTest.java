package product.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import product.Application;
import product.repository.*;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    protected  JdbcTemplate jdbcTemplate;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected BrandRepository brandRepository;

    @Autowired
    protected ImageTypeRepository imageTypeRepository;

    @Autowired
    protected KeyRepository keyRepository;

    @Autowired
    protected AttributeRepository attributeRepository;

    @Autowired
    protected ProductGroupRepository productGroupRepository;

    protected void cleanUp(){
        jdbcTemplate.update("delete from sku_attribute_value");
        jdbcTemplate.update("delete from product_attribute_value");
        jdbcTemplate.update("delete from attribute_value");
        jdbcTemplate.update("delete from category_filter_attribute");
        jdbcTemplate.update("delete from attribute");
        jdbcTemplate.update("delete from sku");
        jdbcTemplate.update("delete from product_image");
        jdbcTemplate.update("delete from image_type");
        jdbcTemplate.update("delete from product_group");
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update("delete from brand");
        jdbcTemplate.update("delete from category");
    }

}
