package com.github.alllef.transportationservice.ui.grid_layout;

import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CostsGridLayoutFactory {
    private final DistanceService distanceService;

    public GridLayout createCostsGridLayout() {
        return new GridLayout(distanceService);
    }
}
