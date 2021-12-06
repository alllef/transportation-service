package com.github.alllef.transportationservice.backend.database.service;

import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.repository.TransportRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransportService {
    private final TransportRepo transportRepo;

    public List<Transport> findAll() {
        return transportRepo.findAll();
    }
}
