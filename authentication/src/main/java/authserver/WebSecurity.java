package authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static java.util.Arrays.asList;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Value("${host.url}")
    private String hostUrl;
    @Value("${security.secret}")
    private String secret;
    @Value("${security.secret.expirationtime}")
    private String expirationTime;

    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/guesttoken").permitAll()
                .antMatchers("/health").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(this.jwtAuthenticationFilter())
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(asList(hostUrl));
        corsConfiguration.setAllowedMethods(asList("GET", "POST", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(asList("Content-Type", "Authentication"));
        corsConfiguration.setExposedHeaders(asList("Authentication"));
        corsConfiguration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(this.authenticationManager(), secret, expirationTime);
    }
}
