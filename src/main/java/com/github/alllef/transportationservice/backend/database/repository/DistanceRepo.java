package com.github.alllef.transportationservice.backend.database.repository;

import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.entity.distance.DistanceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface DistanceRepo extends JpaRepository<Distance, DistanceKey> {

}
