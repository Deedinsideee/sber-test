package ru.alexandr.sbertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SberTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SberTestApplication.class, args);
    }

}
