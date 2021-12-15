package com.github.alllef.transportationservice;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.entity.distance.DistanceKey;
import com.github.alllef.transportationservice.backend.database.repository.ConsumerRepo;
import com.github.alllef.transportationservice.backend.database.repository.DistanceRepo;
import com.github.alllef.transportationservice.backend.database.repository.ProviderRepo;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import lombok.AllArgsConstructor;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@AllArgsConstructor
public class TransportationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransportationServiceApplication.class, args);
    }
}
