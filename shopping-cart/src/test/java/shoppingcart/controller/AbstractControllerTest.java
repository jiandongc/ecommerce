package shoppingcart.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import shoppingcart.Application;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected void reset(){
        jdbcTemplate.update("delete from shopping_cart_item");
        jdbcTemplate.update("delete from shopping_cart");
    }

}
