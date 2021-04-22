package com.example.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@PropertySource("classpath:application.yml")
public class WebAppContext
{
    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
