package com.wime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.wime.model") // Le decimos dónde buscar las entidades
@EnableJpaRepositories("com.wime.repository") // Le decimos dónde buscar los repositorios
public class WimeApplication {
    public static void main(String[] args) {
        SpringApplication.run(WimeApplication.class, args);
    }
}