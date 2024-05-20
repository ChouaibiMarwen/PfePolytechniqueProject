package com.camelsoft.rayaserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class DevCorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:4200","http://localhost:80","https://dashboard.rayafinancing.com","https://raya-admin.camel-soft.com","https://raya-supplier.camel-soft.com","https://meeting.rayafinancing.com","https://rayafinancing.com","https://server.rayafinancing.com", "https://server-raya.camel-soft.com")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }
}
