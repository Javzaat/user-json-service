package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example.demo", "com.example.userjson"})
@EnableJpaRepositories(basePackages = "com.example.userjson.repository")
@EntityScan(basePackages = "com.example.userjson.model")
public class UserJsonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserJsonServiceApplication.class, args);
    }
}