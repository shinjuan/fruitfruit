package com.example.project_03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Import(WebConfig.class)
public class Project03Application {

    public static void main(String[] args) {
        SpringApplication.run(Project03Application.class, args);
    }

}
