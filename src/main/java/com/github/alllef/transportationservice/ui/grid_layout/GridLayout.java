package com.github.alllef.transportationservice.ui.grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.distance.Distance;
import com.github.alllef.transportationservice.backend.database.entity.distance.DistanceKey;
import com.github.alllef.transportationservice.backend.database.service.DistanceService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.shared.Registration;

import java.util.*;

public class GridLayout extends VerticalLayout {
    private final DistanceService distanceService;
    private Grid<GridRow> costsGrid = new Grid<>();
    private Grid<GridRow> distanceGrid = new Grid<>();
    private HorizontalLayout distanceFields;
    private Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity = new HashMap<>();
    private Map<Consumer, Integer> consumersWithCapacity = new HashMap<>();

    private Button calculateResultButton = new Button("Calculate optimal transport shipments");

    public GridLayout(DistanceService distanceService) {
        this.distanceService = distanceService;
        configureCostsGrid();
        configureDistancesGrid();
        add(costsGrid, distanceGrid, calculateResultButton);
        configureButton();
        setSizeFull();
    }

    public void addRow(Provider provider, Transport transport) {
        providersWithTransportAndCapacity.put(provider, Map.entry(transport, 1));
        updateGrids();
    }

    public void onProviderCapacityUpdated(Provider provider, int capacity) {
        Transport providerTransport = providersWithTransportAndCapacity.get(provider)
                .getKey();

        providersWithTransportAndCapacity.put(provider, Map.entry(providerTransport, capacity));
        costsGrid.setItems(getGridRows());
    }

    public void onConsumerCapacityUpdated(Consumer consumer, int capacity) {
        consumersWithCapacity.put(consumer, capacity);
        costsGrid.getColumnByKey(consumer.getName())
                .setHeader(consumer.getName() + ": " + capacity);
    }

    public void removeRow(Provider provider) {
        providersWithTransportAndCapacity.remove(provider);
        updateGrids();
    }

    public void addColumn(Consumer consumer) {
        consumersWithCapacity.put(consumer, 1);

        costsGrid.addColumn(row -> {
                    Provider tmpProvider = row.getProvider();
                    Optional<Distance> distance = distanceService.getDistance(tmpProvider, consumer);
                    return distance.map(value -> distanceService.calcTransportationPrice(value, row.getTransport()))
                            .orElse(-1);
                })
                .setKey(consumer.getName())
                .setHeader(consumer.getName() + ": " + 1)
                .setWidth("5vw");

        distanceGrid.addColumn(row -> {
                    Provider tmpProvider = row.getProvider();
                    Optional<Distance> distance = distanceService.getDistance(tmpProvider, consumer);
                    return distance.map(Distance::getDistance)
                            .orElse(-1);
                })
                .setKey(consumer.getName())
                .setHeader(consumer.getName())
                .setWidth("5vw");

        updateGrids();
    }

    public void removeColumn(Consumer consumer) {
        consumersWithCapacity.remove(consumer);
        costsGrid.removeColumnByKey(consumer.getName());
        distanceGrid.removeColumnByKey(consumer.getName());
        updateGrids();
    }

    private List<GridRow> getGridRows() {
        List<GridRow> rows = new ArrayList<>();
        for (var providerTransportEntry : providersWithTransportAndCapacity.entrySet()) {
            GridRow row = new GridRow(providerTransportEntry.getKey(), providerTransportEntry.getValue().getKey(), providerTransportEntry.getValue().getValue(), consumersWithCapacity);
            rows.add(row);
        }

        return rows;
    }

    private HorizontalLayout getDistanceFields(GridRow row) {
        Provider provider = row.getProvider();
        HorizontalLayout layout = new HorizontalLayout();
        for (Consumer consumer : consumersWithCapacity.keySet()) {
            NumberField numberField = new NumberField(consumer.getName() + "-" + provider.getName());
            Optional<Distance> distance = distanceService.getDistance(row.getProvider(), consumer);
            distance.ifPresent(value -> numberField.setValue((double) value.getDistance()));
            layout.add(numberField);
            numberField.addValueChangeListener(valueChangeEvent -> {
                distanceService.save(new Distance(new DistanceKey(provider.getProviderId(), consumer.getConsumerId()), provider, consumer, valueChangeEvent.getValue().intValue()));
                updateGrids();
            });
        }
        layout.setHeight("20vh");
        return layout;
    }

    private void configureCostsGrid() {
        costsGrid.addColumn(row -> row.getProvider()
                        .getName() + "\n" + row.getProviderCapacity())
                .setKey("Providers")
                .setHeader("Provider names(costs grid)")
                .setWidth("5vw");

        costsGrid.setSizeFull();
        costsGrid.setWidth("50vw");
    }

    private void configureDistancesGrid() {
        distanceGrid.addColumn(row -> row.getProvider()
                        .getName())
                .setKey("Providers")
                .setHeader("Provider names(distances grid)")
                .setWidth("5vw");
        distanceGrid.setSizeFull();
        distanceGrid.setWidth("50vw");

        distanceGrid.asSingleSelect()
                .addValueChangeListener(value -> {
                    if (value.getValue() != null) {
                        if (distanceFields == null) {
                            distanceFields = getDistanceFields(value.getValue());
                            add(distanceFields);
                        }

                        remove(distanceFields);
                        distanceFields = getDistanceFields(value.getValue());
                        add(distanceFields);
                    }
                });
    }

    private void configureButton() {
        calculateResultButton.addClickListener(buttonClickEvent ->
                fireEvent(new CostsGridEvent.CalculationEvent(this)));
    }

    private void updateGrids() {
        costsGrid.setItems(getGridRows());
        distanceGrid.setItems(getGridRows());
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}