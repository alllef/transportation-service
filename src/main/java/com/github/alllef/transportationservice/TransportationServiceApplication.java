package com.github.alllef.transportationservice;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class TransportationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportationServiceApplication.class, args);
    }
}
