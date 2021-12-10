package com.github.alllef.transportationservice.backend.database.service;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.entity.distance.DistanceKey;
import com.github.alllef.transportationservice.backend.database.repository.DistanceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DistanceService {
    private final DistanceRepo distanceRepo;

    public int calcTransportationPrice(Distance distance, Transport transport) {
        return (int) (distance.getDistance() * transport.getFuelConsumptionPerKm());
    }

    public Map<Distance, Integer> getPricesTable(Map<Provider, Transport> providersWithTransport, List<Consumer> consumers) {
        List<Provider> providers = providersWithTransport.keySet()
                .stream()
                .toList();

        List<Distance> distances = this.getDistances(providers, consumers);
        return distances.stream()
                .collect(Collectors.toMap(Function.identity(),(distance)->
                        calcTransportationPrice(distance,providersWithTransport.get(distance.getProvider()))));
    }

    public List<Distance> getDistances(List<Provider> providers, List<Consumer> consumers) {
        Set<DistanceKey> distanceKeys = new HashSet<>();

        for (Provider provider : providers) {
            for (Consumer consumer : consumers)
                distanceKeys.add(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()));
        }

        return distanceRepo.findAllById(distanceKeys);
    }

    public Distance getDistance(Provider provider,Consumer consumer){
        return distanceRepo.findById(new DistanceKey(provider.getProviderId(),consumer.getConsumerId()))
                .orElseThrow();
    }
}
