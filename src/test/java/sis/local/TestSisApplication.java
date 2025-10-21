package sis.local;

import org.springframework.boot.SpringApplication;

public class TestSisApplication {

	public static void main(String[] args) {
		SpringApplication.from(SisApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}