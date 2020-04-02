package com.artemoons.covid19info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class Covid19InfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19InfoApplication.class, args);
    }

}
