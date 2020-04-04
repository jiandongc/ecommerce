package shoppingcart.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String secret) {
        super(authenticationManager);
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String accessToken = req.getHeader("Authentication");

        if (accessToken == null) {
            chain.doFilter(req, res);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String accessToken) {
        try {
            // parse the token.
            final Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(accessToken)
                    .getBody();

            List<String> roles = (List<String>) claims.get("roles");
            roles = roles.stream().map(role -> format("role_%s", role).toUpperCase()).collect(toList());
            return new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    createAuthorityList(roles.toArray(new String[roles.size()])));
        } catch (Exception e) {
            log.info("Failed to parse JWT token : " + accessToken, e);
            return null;
        }
    }
}
