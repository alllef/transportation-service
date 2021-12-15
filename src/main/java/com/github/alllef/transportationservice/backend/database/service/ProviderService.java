package com.github.alllef.transportationservice.backend.database.service;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.repository.ProviderRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProviderService {
    private final ProviderRepo providerRepo;

    public List<Provider> findAll() {
        return providerRepo.findAll();
    }

    public void save(Provider provider) {
        providerRepo.save(provider);
    }
}
