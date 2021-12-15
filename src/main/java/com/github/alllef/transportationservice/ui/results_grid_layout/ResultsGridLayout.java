package com.github.alllef.transportationservice.ui.results_grid_layout;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.TransportAlgo;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultsGridLayout extends VerticalLayout {
    private Text finalCostText = new Text("Final cost result is: ");

    private final Grid<ResultsGridRow> resultsGrid = new Grid<>();
    private final Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity;
    private final Map<Consumer, Integer> consumersWithCapacity;
    private final DistanceService distanceService;

    protected ResultsGridLayout(DistanceService distanceService, Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity, Map<Consumer, Integer> consumersWithCapacity) {
        this.distanceService = distanceService;
        this.providersWithTransportAndCapacity = providersWithTransportAndCapacity;
        this.consumersWithCapacity = consumersWithCapacity;
        configureGrid();
        add(finalCostText, resultsGrid);
        setSizeFull();
        setHeight("auto");
    }

    private void configureGrid() {
        resultsGrid.addColumn(row -> row.getProvider()
                        .getName() + ": " + providersWithTransportAndCapacity.get(row.getProvider()).getValue())
                .setKey("Providers")
                .setHeader("Provider names")
                .setWidth("5vw");

        for (Consumer consumer : consumersWithCapacity.keySet()) {
            resultsGrid.addColumn(row -> {
                        if (row.getConsumersWithShipments().containsKey(consumer))
                            return row.getConsumersWithShipments().get(consumer);
                        return 0;
                    })
                    .setKey(consumer.getName())
                    .setHeader(consumer.getName() + ": " + consumersWithCapacity.get(consumer))
                    .setWidth("5vw");
        }

        resultsGrid.setSizeFull();
        resultsGrid.setHeight("100vh");
        resultsGrid.setItems(getRows());
    }

    private List<ResultsGridRow> getRows() {
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
                .map(consumersWithCapacity::get)
                .toList();

        int[][] costs = getCosts(providers, consumers);

        CostsModel costsModel = new CostsModel(costs, providersCapacity, consumersCapacity);

        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
        transportAlgo.startAlgo();

        finalCostText.setText(finalCostText.getText() + transportAlgo.getTransportationPrice());
        Map<Coords, Integer> nodesWithShipments = transportAlgo.getNodesWithShipments();
        List<ResultsGridRow> rowsList = new ArrayList<>();

        for (int i = 0; i < providers.size(); i++) {
            ResultsGridRow row = new ResultsGridRow(providers.get(i), new HashMap<>());
            for (int j = 0; j < consumers.size(); j++) {
                Coords tmp = new Coords(i, j);
                if (nodesWithShipments.containsKey(tmp))
                    row.getConsumersWithShipments().put(consumers.get(j), nodesWithShipments.get(tmp));
            }
            rowsList.add(row);
        }

        return rowsList;
    }

    private int[][] getCosts(List<Provider> providers, List<Consumer> consumers) {
        int[][] costs = new int[providers.size()][consumers.size()];
        for (int i = 0; i < providers.size(); i++) {
            for (int j = 0; j < consumers.size(); j++) {
                Distance distance = distanceService.getDistance(providers.get(i), consumers.get(j))
                        .orElseThrow();
                costs[i][j] = distanceService.calcTransportationPrice(distance, providersWithTransportAndCapacity.get(providers.get(i)).getKey());
            }
        }

        return costs;
    }
}
