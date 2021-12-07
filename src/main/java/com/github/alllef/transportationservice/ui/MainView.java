package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.ConsumerLayout;
import com.github.alllef.transportationservice.ui.transport_point.ProviderLayout;
import com.github.alllef.transportationservice.ui.transport_point.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.TransportPointLayoutFactory;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@Route("")
@PWA(shortName = "transport-serv", name = "Transportation service")
@RequiredArgsConstructor
public class MainView extends VerticalLayout {
    private final ProviderService providerService;
    private final ConsumerService consumerService;
    private final TransportPointLayoutFactory transportPointLayoutFactory;

    private ComboBox<Provider> providerComboBox = new ComboBox<>("Provider");
    private ComboBox<Consumer> consumerComboBox = new ComboBox<>("Consumer");

    @PostConstruct
    public void afterConstruct() {
        addClassName("list-view");
        setSizeFull();
        configureProviderComboBox();
        configureConsumerComboBox();
        add(providerComboBox, consumerComboBox);
    }

    private void configureProviderComboBox() {
        providerComboBox.setItems(providerService.findAll());
        providerComboBox.setItemLabelGenerator(TransportPoint::getName);
        providerComboBox.addValueChangeListener(event -> {
            ProviderLayout providerLayout = transportPointLayoutFactory.createProviderLayout(event.getValue());
            providerLayout.addListener(TransportPointEvent.DeleteEvent.class,
                    deleteEvent -> this.remove(providerLayout));
            add(providerLayout);
        });
    }

    private void configureConsumerComboBox() {
        consumerComboBox.setItems(consumerService.findAll());
        consumerComboBox.setItemLabelGenerator(Consumer::getName);
        consumerComboBox.addValueChangeListener(event -> {
            ConsumerLayout consumerLayout = transportPointLayoutFactory.createConsumerLayout(event.getValue());
            consumerLayout.addListener(TransportPointEvent.DeleteEvent.class,
                    deleteEvent -> this.remove(consumerLayout));
            add(consumerLayout);
        });
    }

}