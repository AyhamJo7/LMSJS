package com.mavericks.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class for Mavericks Learning Management System.
 * This class bootstraps the Spring Boot application.
 */
@SpringBootApplication
@EnableJpaAuditing
public class MavericksLmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavericksLmsApplication.class, args);
    }
}
