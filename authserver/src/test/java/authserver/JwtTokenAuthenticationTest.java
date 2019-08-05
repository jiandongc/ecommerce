package authserver;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.POST;

public class JwtTokenAuthenticationTest extends AbstractIntegrationTest {

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
