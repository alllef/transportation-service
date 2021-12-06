package com.github.alllef.transportationservice.backend.database.repository;

import com.github.alllef.transportationservice.backend.database.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepo extends JpaRepository<Transport,Long> {
}
