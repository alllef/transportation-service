package com.github.alllef.transportationservice.backend.database.service;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.repository.ConsumerRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConsumerService {
    private final ConsumerRepo consumerRepo;

    public List<Consumer> findAll() {
        return consumerRepo.findAll();
    }
}
