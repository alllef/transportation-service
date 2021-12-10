package com.github.alllef.transportationservice.ui.transport_point.results_grid_layout;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.TransportAlgo;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.*;
import java.util.stream.Collectors;

public class ResultsGridLayout extends VerticalLayout {
    private Grid<CostsGridRow> resultsGrid = new Grid<>();
    private Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity = new HashMap<>();
    private Map<Consumer, Integer> consumersWithCapacity = new HashMap<>();
    private final DistanceService distanceService;

    protected ResultsGridLayout(DistanceService distanceService) {
        this.distanceService = distanceService;
        configureGrid();
        add(resultsGrid);
    }

    private void configureGrid() {
        resultsGrid.addColumn(row -> row.getProvider()
                        .getName())
                .setKey("Providers")
                .setHeader("Provider names");

        for (Consumer consumer : consumersWithCapacity.keySet()) {
            resultsGrid.addColumn(row -> row.getConsumersWithTransportShipments().get(consumer))
                    .setKey(consumer.getName())
                    .setHeader(consumer.getName());
        }

        resultsGrid.setSizeFull();
        resultsGrid.setItems(getRows());
    }

    private List<CostsGridRow> getRows() {
        List<Provider> providers = providersWithTransportAndCapacity.keySet()
                .stream()
                .toList();

        List<Consumer> consumers = consumersWithCapacity.keySet()
                .stream()
                .toList();

        List<Integer> providersCapacity = providers.stream()
                .map(provider -> providersWithTransportAndCapacity.get(provider)
                        .getValue())
                .collect(Collectors.toList());

        List<Integer> consumersCapacity = consumers.stream()
                .map(consumer -> consumersWithCapacity.get(consumer))
                .toList();

        int[][] costs = getCosts(providers, consumers);

        CostsModel costsModel = new CostsModel(costs, providersCapacity, consumersCapacity);

        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
        transportAlgo.startAlgo();

        Map<Coords, Integer> nodesWithShipments = transportAlgo.getNodesWithShipments();
        List<CostsGridRow> rowsList = new ArrayList<>();

        for (int i = 0; i < providers.size(); i++) {
            CostsGridRow row = new CostsGridRow(providers.get(i), providersWithTransportAndCapacity.get(providers.get(i)).getKey(), new HashMap<>());
            for (int j = 0; j < consumers.size(); j++) {
                Coords tmp = new Coords(i, j);
                if (nodesWithShipments.containsKey(tmp))
                    row.getConsumersWithTransportShipments().put(consumers.get(j), nodesWithShipments.get(tmp));
            }
        }

        return rowsList;
    }

    private int[][] getCosts(List<Provider> providers, List<Consumer> consumers) {
        int[][] costs = new int[providers.size()][consumers.size()];
        for (int i = 0; i < providers.size(); i++) {
            for (int j = 0; j < consumers.size(); j++) {
                Distance distance = distanceService.getDistance(providers.get(i), consumers.get(j));
                costs[i][j] = distanceService.calcTransportationPrice(distance, providersWithTransportAndCapacity.get(providers.get(i)).getKey());
            }
        }

        return costs;
    }
}
