package customer.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by jiandong on 20/05/15.
 */

@Configuration
@ComponentScan(basePackages = { "customer.controller", "customer.service"})
@EnableJpaRepositories(basePackages = "customer.repository")
@EntityScan(basePackages="customer.domain")
@PropertySource({"classpath:application.properties", "classpath:test.properties"})
@EnableAutoConfiguration
public class TestApplicationContext {
    public static void main(String[] args){
        SpringApplication.run(TestApplicationContext.class, args);
    }
}
