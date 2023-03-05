package com.example.filmography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class FilmographyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmographyApplication.class, args);
        log.info("Swagger-ui run on: http://localhost:9090/swagger-ui/index.html");

    }
}
