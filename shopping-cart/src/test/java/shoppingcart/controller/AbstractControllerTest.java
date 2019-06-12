package shoppingcart.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shoppingcart.Application;

import java.util.Random;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected HttpHeaders headers;
    private Random random = new Random();

    @Before
    public void before() {
        this.reset();

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
    public void reset(){
        jdbcTemplate.update("delete from address");
        jdbcTemplate.update("delete from shopping_cart_item");
        jdbcTemplate.update("delete from delivery_option");
        jdbcTemplate.update("delete from shopping_cart");
    }
}
