package authserver;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractIntegrationTest {

    protected final String BASE_URL = "http://localhost:9999/login";
    protected final TestRestTemplate rest = new TestRestTemplate();
    protected HttpHeaders headers;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Before
    public void setup(){
        this.cleanup();
        headers = new HttpHeaders();

        Random random = new Random();
        if(random.nextInt() % 2 == 1){
            // user token which will expire in 100 years
            headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGVuQGdtYWlsLmNvbSIsInJvbGVzIjpbInVzZXIiXSwiZXhwIjo0NjY4MzgzNDM3fQ.xjlZBzvqJ1fmfFupB1FMWXCBODlLf6aslnidRP1d1fPvgfc0cS7tyRikkk-KBVlf8n17O3vZgEPlAjw5lSiuiA");
        } else {
            // guest token which will expire in 100 years
            headers.set("Authentication", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsInJvbGVzIjpbImd1ZXN0Il0sImV4cCI6NDY2ODM4MzY2Nn0.LB82m9mCmxIipOAR7mx58MUoeBBDBeIF4mP4kcOpHZvy5RyYhBiL5C5AJP3j8YNCMWaMAVANP6zrlU8031oBMA");
        }
    }

    @After
    public void cleanup(){
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update("delete from token");
        jdbcTemplate.update("delete from address");
        jdbcTemplate.update("delete from customer");
    }
}
