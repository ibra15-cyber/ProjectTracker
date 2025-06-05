package com.ibra.projecttracker;

import jakarta.persistence.Cacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProjectTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectTrackerApplication.class, args);
    }

}
