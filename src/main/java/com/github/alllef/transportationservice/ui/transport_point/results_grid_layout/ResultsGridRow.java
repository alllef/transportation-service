package com.github.alllef.transportationservice.ui.transport_point.results_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class ResultsGridRow {
    private Provider provider;
    private Map<Consumer,Integer> consumersWithShipments;
}
