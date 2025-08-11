package org.example.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Payment Service application.
 * 
 * @SpringBootApplication annotation enables:
 * - Auto-configuration
 * - Component scanning
 * - Additional Spring Boot features
 */
@SpringBootApplication
public class PaymentServiceApplication {

    public static void main(String[] args) {
        // Bootstrap the application using Spring Boot's SpringApplication class
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}
