package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.ConsumerManagerLayout;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.ProviderManagerLayout;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.TransportPointManagerLayout;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.TransportPointManagerLayoutFactory;
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

    @PostConstruct
    public void afterConstruct() {
        addClassName("list-view");
        setSizeFull();
        add(new HorizontalLayout(transportPointManagerLayoutFactory.createProviderManagerLayout(), transportPointManagerLayoutFactory.createConsumerManagerLayout()));
    }

}