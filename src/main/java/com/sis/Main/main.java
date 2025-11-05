package com.sis.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.sis")
@EntityScan(basePackages = "com.sis.Model")
@EnableJpaRepositories(basePackages = "com.sis.Repo")

public class main {

    public static void main(String[] args) {
        SpringApplication.run(main.class, args);
    }

}