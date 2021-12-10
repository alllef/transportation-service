package com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.Map;

public class CostsGridLayout extends VerticalLayout {
    private final DistanceService distanceService;
    Grid<CostsGridRow> costsGrid = new Grid<>();

    public CostsGridLayout(DistanceService distanceService) {
        this.distanceService = distanceService;
        configureGrid();
        add(costsGrid);
    }

    public void addColumn(Consumer consumer) {
        costsGrid.addColumn(row -> {
                    Provider tmpProvider = row.getProvider();
                    Distance distance = distanceService.getDistance(tmpProvider, consumer);
                    return distanceService.calcTransportationPrice(distance, row.getTransport());
                })
                .setKey(consumer.getName())
                .setHeader(consumer.getName());
    }

    private void addRow(Provider provider) {

    }

    private void configureGrid() {
        costsGrid.addColumn(row->row.getProvider().getName())
                .setKey("Providers")
                .setHeader("Provider names");
    }
}
