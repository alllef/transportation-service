package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridEvent;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridLayout;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridLayoutFactory;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.*;
import com.github.alllef.transportationservice.ui.transport_point.results_grid_layout.ResultsGridLayout;
import com.github.alllef.transportationservice.ui.transport_point.results_grid_layout.ResultsGridLayoutFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@Route("")
@PWA(shortName = "transport-serv", name = "Transportation service")
@RequiredArgsConstructor
public class MainView extends VerticalLayout {
    private final TransportPointManagerLayoutFactory transportPointManagerLayoutFactory;
    private final CostsGridLayoutFactory costsGridLayoutFactory;
    private final ResultsGridLayoutFactory resultsGridLayoutFactory;

    private ProviderManagerLayout providerManagerLayout;
    private ConsumerManagerLayout consumerManagerLayout;
    private CostsGridLayout costsGridLayout;
    private ResultsGridLayout resultsGridLayout;

    @PostConstruct
    public void afterConstruct() {
        createLayouts();
        configureListeners();

        addClassName("list-view");
        setSizeFull();
        add(new HorizontalLayout(providerManagerLayout, costsGridLayout, consumerManagerLayout));
    }

    private void createLayouts() {
        providerManagerLayout = transportPointManagerLayoutFactory.createProviderManagerLayout();
        consumerManagerLayout = transportPointManagerLayoutFactory.createConsumerManagerLayout();
        costsGridLayout = costsGridLayoutFactory.createCostsGridLayout();
    }

    private void configureListeners() {
        providerManagerLayout.addListener(TransportPointManagerEvent.ProviderAddEvent.class, event -> costsGridLayout.addRow(event.getProvider(), event.getTransport()));
        providerManagerLayout.addListener(TransportPointManagerEvent.ProviderDeleteEvent.class, event -> costsGridLayout.removeRow(event.getProvider()));
        providerManagerLayout.addListener(TransportPointManagerEvent.ProviderCapacityChangedEvent.class, event -> costsGridLayout.onProviderCapacityUpdated(event.getProvider(), event.getCapacity()));

        consumerManagerLayout.addListener(TransportPointManagerEvent.ConsumerAddEvent.class, event -> costsGridLayout.addColumn(event.getConsumer()));
        consumerManagerLayout.addListener(TransportPointManagerEvent.ConsumerDeleteEvent.class, event -> costsGridLayout.removeColumn(event.getConsumer()));
        consumerManagerLayout.addListener(TransportPointManagerEvent.ConsumerCapacityChangedEvent.class, event -> costsGridLayout.onConsumerCapacityUpdated(event.getConsumer(), event.getCapacity()));

        costsGridLayout.addListener(CostsGridEvent.CalculationEvent.class, calculationEvent -> {
            if (resultsGridLayout == null) {
                resultsGridLayout = resultsGridLayoutFactory.createResultsGridLayout(providerManagerLayout.getProvidersWithTransportAndCapacity(), consumerManagerLayout.getUsedTransportPoints());
                add(resultsGridLayout);
            }
            resultsGridLayout = resultsGridLayoutFactory.createResultsGridLayout(providerManagerLayout.getProvidersWithTransportAndCapacity(), consumerManagerLayout.getUsedTransportPoints());
        });
    }
}