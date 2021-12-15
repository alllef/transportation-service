package com.github.alllef.transportationservice.ui.results_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor

public class ResultsGridRow {
    private Provider provider;
    private Map<Consumer,Integer> consumersWithShipments;
}
