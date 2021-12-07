package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import com.github.alllef.transportationservice.ui.transport_point.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.TransportPointLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import io.netty.handler.codec.http2.Http2GoAwayFrame;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@Route("")
@PWA(shortName = "transport-serv", name = "Transportation service")
@RequiredArgsConstructor
public class MainView extends VerticalLayout {
    private final TransportService transportService;
    private final ProviderService providerService;

    private ComboBox<Provider> providerComboBox = new ComboBox<>("Provider");
    private ComboBox<Consumer> consumerComboBox = new ComboBox<>("Consumer");

    @PostConstruct
    public void afterConstruct() {
        addClassName("list-view");
        setSizeFull();
        configureProviderComboBox();
        add(providerComboBox);
    }

    private void configureProviderComboBox() {
        providerComboBox.setItems(providerService.findAll());
        providerComboBox.setItemLabelGenerator(Provider::getName);
        providerComboBox.addValueChangeListener(event -> {
            TransportPointLayout<Provider> transportPointLayout = new TransportPointLayout<>(event.getValue());
            transportPointLayout.addListener(TransportPointEvent.DeleteEvent.class, deleteEvent -> this.remove(transportPointLayout));
            add(transportPointLayout);
        });
    }

}