package com.github.alllef.transportationservice.ui.results_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ResultsGridLayoutFactory {
    private final DistanceService distanceService;

    public ResultsGridLayout createResultsGridLayout(Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity, Map<Consumer, Integer> consumersWithCapacity) {
        return new ResultsGridLayout(distanceService,providersWithTransportAndCapacity,consumersWithCapacity);
    }
}
