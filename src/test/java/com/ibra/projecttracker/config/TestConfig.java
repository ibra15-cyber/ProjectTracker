package com.ibra.projecttracker.config;


import com.ibra.projecttracker.service.ProjectService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    public ProjectService mockProjectService() {
        return Mockito.mock(ProjectService.class);
    }
}