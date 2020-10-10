package authserver;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.POST;

public class JwtTokenAuthenticationTest extends AbstractIntegrationTest {

    @Value("${security.secret}")
    private String secret;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void shouldReturnJasonWebTokenIfUserProvideValidUsernameAndPassword() {
        // Given
        String hashedPassword = passwordEncoder.encode("12345");
        final String sql = "insert into customer (name, email, password, customer_uid) values ('chen', 'chen@gmail.com', '" + hashedPassword + "', '"+ UUID.randomUUID() + "')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = rest.exchange(BASE_URL, POST, data, String.class);

        // Then
        final Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(exchange.getHeaders().get("Authentication").get(0))
                .getBody();
        final List<String> roles = (List<String>) claims.get("roles");
        assertThat(roles.size(), is(1));
        assertThat(roles.get(0), is("user"));
    }

    @Test(expected = ResourceAccessException.class)
    public void shouldNotReturnJasonWebTokenIfPasswordIsIncorrect() {
        // Given
        String hashedPassword = passwordEncoder.encode("12345");
        final String sql = "insert into customer (name, email, password, customer_uid) values ('chen', 'chen@gmail.com', '" + hashedPassword + "', '"+ UUID.randomUUID() + "')";
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
    public void shouldNotReturnJasonWebTokenIfEmailIsIncorrect() {
        // Given
        String hashedPassword = passwordEncoder.encode("12345");
        final String sql = "insert into customer (name, email, password, customer_uid) values ('chen', 'chen@gmail.com', '" + hashedPassword + "', '"+ UUID.randomUUID() + "')";
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
    public void shouldNotReturnJasonWebTokenIfRequestDoesNotHaveAValidAccessToken() {
        // Given
        headers.remove("Authentication");
        String hashedPassword = passwordEncoder.encode("12345");
        final String sql = "insert into customer (name, email, password, customer_uid) values ('chen', 'chen@gmail.com', '" + hashedPassword + "', '"+ UUID.randomUUID() + "')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"chen@gmail.com\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        rest.exchange(BASE_URL, POST, data, String.class);
    }

    @Test
    public void shouldReturnAdminToken() {
        // Given
        String hashedPassword = passwordEncoder.encode("12345");
        final String sql = "insert into customer (name, email, password, customer_uid) values ('chen', 'admin.one@noodle-monster.co.uk', '" + hashedPassword + "', '"+ UUID.randomUUID() + "')";
        jdbcTemplate.execute(sql);

        // When
        String json = "{\n" +
                "    \"username\": \"admin.one@noodle-monster.co.uk\",\n" +
                "    \"password\": \"12345\"\n" +
                "  }";
        HttpEntity<String> data = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = rest.exchange(BASE_URL, POST, data, String.class);

        // Then
        final Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(exchange.getHeaders().get("Authentication").get(0))
                .getBody();
        final List<String> roles = (List<String>) claims.get("roles");
        assertThat(roles.size(), is(2));
        assertThat(roles.get(0), is("admin"));
        assertThat(roles.get(1), is("user"));
    }
}
