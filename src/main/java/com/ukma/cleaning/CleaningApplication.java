package com.ukma.cleaning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class CleaningApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleaningApplication.class, args);
	}
}
