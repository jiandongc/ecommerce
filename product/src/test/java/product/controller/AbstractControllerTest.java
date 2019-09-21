package product.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import product.Application;
import product.repository.*;

import java.util.Random;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractControllerTest {

    private Random random = new Random();

    @Autowired
    protected  JdbcTemplate jdbcTemplate;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected BrandRepository brandRepository;

    @Autowired
    protected KeyRepository keyRepository;

    @Autowired
    protected AttributeRepository attributeRepository;

    @Autowired
    protected ProductGroupRepository productGroupRepository;

    protected HttpHeaders headers = null;

    @Before
    public void before(){
        this.cleanUp();
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(APPLICATION_JSON);
            if(random.nextInt() % 2 == 1){
                // user token which will expire in 100 years
                headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
            } else {
                // guest token which will expire in 100 years
                headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
            }
        }
    }

    @After
    public void cleanUp(){
        jdbcTemplate.update("delete from sku_attribute");
        jdbcTemplate.update("delete from product_attribute_value");
        jdbcTemplate.update("delete from attribute_value");
        jdbcTemplate.update("delete from category_filter_attribute");
        jdbcTemplate.update("delete from attribute");
        jdbcTemplate.update("delete from price");
        jdbcTemplate.update("delete from sku");
        jdbcTemplate.update("delete from product_image");
        jdbcTemplate.update("delete from product_group");
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update("delete from brand");
        jdbcTemplate.update("delete from category");
    }

}
