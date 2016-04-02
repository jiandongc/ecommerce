package order.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = { "order.controller", "order.service", "order.mapper"})
@EnableJpaRepositories(basePackages = "order.repository")
@EntityScan(basePackages="order.domain")
@PropertySource({"classpath:application.properties", "classpath:test.properties"})
@EnableAutoConfiguration
public class TestApplicationContext {
    public static void main(String[] args){
        SpringApplication.run(TestApplicationContext.class, args);
    }
}
