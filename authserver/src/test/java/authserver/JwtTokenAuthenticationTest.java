package authserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResourceAccessException;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public class JwtTokenAuthenticationTest {

    private final String BASE_URL = "http://localhost:9999/login";
    private final TestRestTemplate rest = new TestRestTemplate();
    private HttpHeaders headers;
    private Random random;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup(){
        jdbcTemplate.update("delete from customer");
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
        jdbcTemplate.update("delete from customer");
    }

    @Test
    public void shouldReturnJasonWebTokenIfUserProvideValidUsernameAndPassword(){
        // Given
        final String sql = "insert into customer (name, email, password) values ('chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = rest.exchange(BASE_URL, POST, data, String.class);

        // Then
        assertThat(exchange.getHeaders().containsKey("Authentication"), is(true));
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfPasswordIsIncorrect(){
        // Given
        final String sql = "insert into customer (name, email, password) values ('chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"1234567\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        rest.exchange(BASE_URL, POST, data, String.class);
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfEmailIsIncorrect(){
        // Given
        final String sql = "insert into customer (name, email, password) values ('chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chenjiandong@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        rest.exchange(BASE_URL, POST, data, String.class);
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfRequestDoesNotHaveAValidAccessToken(){
        // Given
        headers.remove("Authentication");
        final String sql = "insert into customer (name, email, password) values ('chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        rest.exchange(BASE_URL, POST, data, String.class);
    }
}
