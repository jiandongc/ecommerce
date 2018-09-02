package authserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResourceAccessException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public class JwtTokenAuthenticationTest {

    private final String BASE_URL = "http://localhost:9999/login";
    private final TestRestTemplate rest = new TestRestTemplate();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup(){
        jdbcTemplate.update("delete from customer");
    }

    @After
    public void cleanup(){
        jdbcTemplate.update("delete from customer");
    }

    @Test
    public void shouldReturnJasonWebTokenIfUserProvideValidUsernameAndPassword(){
        // Given
        final String sql = "insert into customer (id, name, email, password) values (nextval('customer_seq'), 'chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json);
        ResponseEntity<String> exchange = rest.exchange(BASE_URL, POST, data, String.class);

        // Then
        assertThat(exchange.getHeaders().containsKey("Authentication"), is(true));
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfPasswordIsIncorrect(){
        // Given
        final String sql = "insert into customer (id, name, email, password) values (nextval('customer_seq'), 'chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"1234567\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json);
        rest.exchange(BASE_URL, POST, data, String.class);
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfUserNameIsIncorrect(){
        // Given
        final String sql = "insert into customer (id, name, email, password) values (nextval('customer_seq'), 'chen', 'chen@gmail.com', '12345')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chenjiandong@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json);
        rest.exchange(BASE_URL, POST, data, String.class);
    }

}
