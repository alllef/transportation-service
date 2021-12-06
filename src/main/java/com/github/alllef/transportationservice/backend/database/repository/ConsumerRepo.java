package com.github.alllef.transportationservice.backend.database.repository;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepo  extends JpaRepository<Consumer,Long> {

}
