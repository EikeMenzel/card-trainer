package com.service.mailservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.SpringApplication.run;

@EnableScheduling
@SpringBootApplication
public class MailServiceApplication {

    public static void main(String[] args) {
        run(MailServiceApplication.class, args);
    }

}
