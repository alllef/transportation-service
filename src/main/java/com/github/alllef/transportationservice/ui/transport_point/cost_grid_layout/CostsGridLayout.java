package com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.*;

public class CostsGridLayout extends VerticalLayout {
    private final DistanceService distanceService;
    private Grid<CostsGridRow> costsGrid = new Grid<>();

    private Map<Provider, Transport> providersWithTransport = new HashMap<>();
    private Set<Consumer> consumers = new HashSet<>();

    private Button calculateResultButton = new Button("Calculate optimal transport shipments");

    public CostsGridLayout(DistanceService distanceService) {
        this.distanceService = distanceService;
        configureGrid();
        add(costsGrid, calculateResultButton);
        configureButton();
    }

    public void addRow(Provider provider, Transport transport) {
        providersWithTransport.put(provider, transport);
        costsGrid.setItems(getCostsGridRows());
    }

    public void removeRow(Provider provider) {
        providersWithTransport.remove(provider);
        costsGrid.setItems(getCostsGridRows());
    }

    public void addColumn(Consumer consumer) {
        consumers.add(consumer);

        costsGrid.addColumn(row -> {
                    Provider tmpProvider = row.getProvider();
                    Distance distance = distanceService.getDistance(tmpProvider, consumer);
                    return distanceService.calcTransportationPrice(distance, row.getTransport());
                })
                .setKey(consumer.getName())
                .setHeader(consumer.getName());

        costsGrid.setItems(getCostsGridRows());
    }

    public void removeColumn(Consumer consumer) {
        consumers.remove(consumer);
        costsGrid.removeColumnByKey(consumer.getName());
        costsGrid.setItems(getCostsGridRows());
    }

    private List<CostsGridRow> getCostsGridRows() {
        List<CostsGridRow> rows = new ArrayList<>();
        for (var providerTransportEntry : providersWithTransport.entrySet()) {
            CostsGridRow row = new CostsGridRow(providerTransportEntry.getKey(), providerTransportEntry.getValue(), consumers);
            rows.add(row);
        }

        return rows;
    }

    private void configureGrid() {
        costsGrid.addColumn(row -> row.getProvider()
                        .getName())
                .setKey("Providers")
                .setHeader("Provider names");

        costsGrid.setSizeFull();
    }

    private void configureButton() {
        calculateResultButton.addClickListener(buttonClickEvent ->
                fireEvent(new CostsGridEvent.CalculationEvent(this)));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
