package com.github.alllef.transportationservice.ui.grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class GridRow {
    private Provider provider;
    private Transport transport;
    private int providerCapacity;
    private Map<Consumer,Integer> consumersWithNeeds;
}