package com.multi.bungae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class BungaeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BungaeApplication.class, args);
    }
}
