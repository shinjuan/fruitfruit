package com.example.project_03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Project03Application {

    public static void main(String[] args) {
        SpringApplication.run(Project03Application.class, args);
    }

}
