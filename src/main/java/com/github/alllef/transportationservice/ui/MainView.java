package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridLayout;
import com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout.CostsGridLayoutFactory;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.*;
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

    private ProviderManagerLayout providerManagerLayout;
    private ConsumerManagerLayout consumerManagerLayout;
    private CostsGridLayout costsGridLayout;

    @PostConstruct
    public void afterConstruct() {
        providerManagerLayout = transportPointManagerLayoutFactory.createProviderManagerLayout();
        consumerManagerLayout = transportPointManagerLayoutFactory.createConsumerManagerLayout();
        costsGridLayout = costsGridLayoutFactory.createCostsGridLayout();
                consumerManagerLayout.addListener(TransportPointManagerEvent.ConsumerAddEvent.class, event -> costsGridLayout.addColumn(event.getConsumer()));
        providerManagerLayout.addListener(TransportPointManagerEvent.ProviderAddEvent.class, event -> costsGridLayout.addRow(event.getProvider(), event.getTransport()));

        addClassName("list-view");
        setSizeFull();
        add(new HorizontalLayout(providerManagerLayout, costsGridLayout, consumerManagerLayout));
    }

}