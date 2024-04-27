package com.brlopes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The ApiServicebankApplication class is the entry point of the Spring Boot application.
 * It uses the @SpringBootApplication annotation to enable auto-configuration and component scanning.
 */
@SpringBootApplication
public class ApiServicebankApplication {

    /**
     * The main method starts the Spring Boot application.
     * It calls the run method of the SpringApplication class, passing ApiServicebankApplication.class as an argument.
     * The args parameter contains command-line arguments.
     *
     * @param args command-line arguments.
     */
	public static void main(String[] args) {
		SpringApplication.run(ApiServicebankApplication.class, args);
	}
}