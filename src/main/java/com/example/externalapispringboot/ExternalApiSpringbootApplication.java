package com.example.externalapispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.externalapispringboot.controller",
                             "com.example.externalapispringboot.service",
                             "com.example.externalapispringboot.config"})
public class ExternalApiSpringbootApplication implements WebMvcConfigurer {
	public static void main(String[] args) {

		SpringApplication.run(ExternalApiSpringbootApplication.class, args);
	}
}
