package com.github.alllef.transportationservice;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import lombok.AllArgsConstructor;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class TransportationServiceApplication {
    private final ProviderService providerService;

    public static void main(String[] args) {
        SpringApplication.run(TransportationServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomething(){
        List<Provider> entities = providerService.findAll();
    }
}
