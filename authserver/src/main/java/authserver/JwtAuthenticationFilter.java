package authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final String secret;
    private final String expirationTime;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String secret,
                                   String expirationTime) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        if (this.hasRequiredAccessToken(req)) {
            try {
                final ApplicationUser user = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new AuthenticationServiceException("access token required");
        }
    }

    private boolean hasRequiredAccessToken(HttpServletRequest req) {
        final String accessToken = req.getHeader("Authentication");

        if(accessToken == null){
            return false;
        }

        final Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(accessToken)
                .getBody();

        if (claims == null) {
            return false;
        }

        final List<String> roles = (List<String>) claims.get("roles");
        return roles.stream().anyMatch(role -> role.equalsIgnoreCase("guest") || role.equalsIgnoreCase("user"));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        final List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .signWith(HS512, secret.getBytes())
                .claim("roles", roles)
                .setExpiration(new Date(currentTimeMillis() + parseLong(expirationTime)));

        res.addHeader("Authentication", jwtBuilder.compact());
    }
}
