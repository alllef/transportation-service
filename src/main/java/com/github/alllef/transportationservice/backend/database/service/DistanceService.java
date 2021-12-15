package com.github.alllef.transportationservice.backend.database.service;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.entity.distance.DistanceKey;
import com.github.alllef.transportationservice.backend.database.repository.ConsumerRepo;
import com.github.alllef.transportationservice.backend.database.repository.DistanceRepo;
import com.github.alllef.transportationservice.backend.database.repository.ProviderRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DistanceService {
    private final DistanceRepo distanceRepo;
    private final ProviderRepo providerRepo;
    private final ConsumerRepo consumerRepo;

    public int calcTransportationPrice(Distance distance, Transport transport) {
        return (int) (distance.getDistance() * transport.getFuelConsumptionPerKm());
    }

    public Map<Distance, Integer> getPricesTable(Map<Provider, Transport> providersWithTransport, List<Consumer> consumers) {
        List<Provider> providers = providersWithTransport.keySet()
                .stream()
                .toList();

        List<Distance> distances = this.getDistances(providers, consumers);
        return distances.stream()
                .collect(Collectors.toMap(Function.identity(), (distance) ->
                        calcTransportationPrice(distance, providersWithTransport.get(distance.getProvider()))));
    }

    public List<Distance> getDistances(List<Provider> providers, List<Consumer> consumers) {
        Set<DistanceKey> distanceKeys = new HashSet<>();

        for (Provider provider : providers) {
            for (Consumer consumer : consumers)
                distanceKeys.add(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()));
        }

        return distanceRepo.findAllById(distanceKeys);
    }

    public Optional<Distance> getDistance(Provider provider, Consumer consumer) {
        return distanceRepo.findById(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()));
    }

    public void save(Distance distance) {
        distanceRepo.save(distance);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void populateWithDistanceData() {
        List<Provider> providers = providerRepo.findAll();
        List<Consumer> consumers = consumerRepo.findAll();
        List<Distance> distances = new ArrayList<>();
        List<Integer> distanceNums = List.of(500, 300, 400, 200);
        int distNum = 0;

        for (Provider provider : providers) {
            for (Consumer consumer : consumers) {
                int number = distanceNums.get(distNum);
                distances.add(new Distance(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()), provider, consumer, number));
                distNum++;
            }
        }

        distanceRepo.saveAll(distances);
    }
}
