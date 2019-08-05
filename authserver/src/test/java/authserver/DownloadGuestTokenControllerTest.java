package authserver;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DownloadGuestTokenControllerTest extends AbstractIntegrationTest {

    @Test
    public void shouldDownloadGuestToken(){
        final HttpEntity<String> payload = new HttpEntity<>(headers);
        final ResponseEntity<String> response = rest.exchange("http://localhost:9999/guesttoken", HttpMethod.GET, payload, String.class);
        assertThat(response.getStatusCode(), Matchers.is(HttpStatus.OK));
        assertThat(response.getHeaders().containsKey("Authentication"), is(true));
    }
}