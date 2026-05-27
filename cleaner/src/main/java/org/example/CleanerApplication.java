package org.example;

import org.example.cleaner.CleanerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(CleanerProperties.class)
public class CleanerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleanerApplication.class, args);
    }
}
