package com.github.alllef.transportationservice.ui.cost_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CostsGridRow {
    private Provider provider;
    private Transport transport;
    private int providerCapacity;
    private Map<Consumer,Integer> consumersWithNeeds;
}