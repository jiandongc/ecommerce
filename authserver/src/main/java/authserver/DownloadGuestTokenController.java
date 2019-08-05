package authserver;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/guesttoken")
public class DownloadGuestTokenController {

    @Value("${security.secret}")
    private String secret;
    @Value("${security.secret.expirationtime}")
    private String expirationTime;

    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> downloadGuestToken() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject("guest_user")
                .signWith(HS512, secret.getBytes())
                .claim("roles", Arrays.asList("GUEST"))
                .setExpiration(new Date(currentTimeMillis() + parseLong(expirationTime)));
        responseHeaders.set("Authentication", jwtBuilder.compact());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("guest token");
    }
}
