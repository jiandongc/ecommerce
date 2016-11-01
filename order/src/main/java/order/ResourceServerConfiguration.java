package order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${token.store.driverClassName}")
    private String driverClassName;
    @Value("${token.store.url}")
    private String url;
    @Value("${token.store.username}")
    private String username;
    @Value("${token.store.password}")
    private String password;

    private DataSource dataSource;

    @PostConstruct
    private void initialize(){
        dataSource = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }


    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous().and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/anoncarts/**").permitAll()
                .antMatchers(HttpMethod.POST, "/anoncarts").permitAll()
                .antMatchers(HttpMethod.DELETE, "/anoncarts/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/anoncarts/**").permitAll()
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }
}
