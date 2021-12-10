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
    private final DistanceRepo distanceRepo;
    private final ProviderRepo providerRepo;
    private final ConsumerRepo consumerRepo;


    public static void main(String[] args) {
        SpringApplication.run(TransportationServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateWithDistanceData() {
        List<Provider> providers = providerRepo.findAll();
        List<Consumer> consumers = consumerRepo.findAll();
        List<Distance> distances = new ArrayList<>();

        for (Provider provider : providers) {
            for (Consumer consumer : consumers) {
                Random random = new Random();
                int number = random.nextInt(5) + 1;
                distances.add(new Distance(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()), provider, consumer, number));
            }
        }

        distanceRepo.saveAll(distances);
    }
}
