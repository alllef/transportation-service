package com.github.alllef.transportationservice.backend.database.repository;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepo extends JpaRepository<Provider,Long> {
}
