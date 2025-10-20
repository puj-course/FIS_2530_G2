package com.sis.Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sis")
public class main {

    public static void main(String[] args) {
        SpringApplication.run(main.class, args);
    }

}