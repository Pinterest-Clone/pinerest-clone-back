package com.sparta.pinterest_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PinterestCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinterestCloneApplication.class, args);
    }

}
