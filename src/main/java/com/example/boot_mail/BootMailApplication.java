package com.example.boot_mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BootMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootMailApplication.class, args);
    }

}
