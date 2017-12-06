package authserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

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
        try {
            final ApplicationUser user = new ObjectMapper().readValue(req.getInputStream(), ApplicationUser.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        final JwtBuilder jwtBuilder = Jwts.builder().setSubject(((User) auth.getPrincipal()).getUsername())
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .signWith(HS512, secret.getBytes())
                .claim("role", auth.getAuthorities());

        if(auth.getAuthorities().size() == 1 & auth.getAuthorities().contains(new SimpleGrantedAuthority("guest"))){
            jwtBuilder.setExpiration(new Date(currentTimeMillis() + parseLong(expirationTime) * 365));
        } else {
            jwtBuilder.setExpiration(new Date(currentTimeMillis() + parseLong(expirationTime)));
        }

        res.addCookie(new Cookie("access_token", jwtBuilder.compact()));
    }
}