package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ProviderFormEvent;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.layout.ProviderFormLayout;
import com.vaadin.flow.component.ClickEvent;

import java.util.HashMap;
import java.util.Map;

public class ProviderManagerLayout extends TransportPointManagerLayout<Provider> {
    private final ProviderService providerService;
    private Map<Provider, Transport> providerTransportMap = new HashMap<>();

    public ProviderManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory, ProviderService providerService) {
        super(transportPointLayoutFactory);
        this.providerService = providerService;
        choosePointComboBox.setItems(providerService.findAll());
    }

    @Override
    protected void configureChoosePointComboBox() {
        choosePointComboBox.setLabel("Choose provider");
        choosePointComboBox.setItemLabelGenerator(item -> String.format("%s(max capacity: %s)", item.getName(), item.getMaxCapacity()));
    }

    @Override
    protected void configureCreateButton() {
        createButton.setText("Create provider");
        createButton.addClickListener(event -> {
            ProviderFormLayout providerFormLayout = new ProviderFormLayout();
            addComponentAtIndex(3,providerFormLayout);
            providerFormLayout.addListener(ProviderFormEvent.ProviderSaveEvent.class, saveEvent -> {
                providerService.save(saveEvent.getProvider());
                remove(providerFormLayout);
                resetComboBoxValues(providerService.findAll());
            });
            providerFormLayout.addListener(ProviderFormEvent.ProviderFormCloseEvent.class, closeEvent -> remove(providerFormLayout));
        });
    }

    @Override
    protected void onButtonClicked(ClickEvent<?> clickEvent) {
        super.onButtonClicked(clickEvent);
        resetComboBoxValues(providerService.findAll());
    }

    @Override
    protected void onDeleteEvent(TransportPointEvent.DeleteEvent deleteEvent) {
        if (deleteEvent.getTransportPoint() instanceof Provider provider) {
            usedTransportPoints.remove(provider);
            fireEvent(new TransportPointManagerEvent.ProviderDeleteEvent(this, provider));
            resetComboBoxValues(providerService.findAll());
        }
    }

    @Override
    protected void onProviderConfiguredEvent(TransportPointEvent.ProviderConfiguredEvent event) {
        providerTransportMap.put(event.getProvider(), event.getTransport());
    }

    public Map<Provider, Map.Entry<Transport, Integer>> getProvidersWithTransportAndCapacity() {
        Map<Provider, Map.Entry<Transport, Integer>> providersWithTransportAndCapacity = new HashMap<>();
        for (Provider provider : providerTransportMap.keySet()) {
            providersWithTransportAndCapacity.put(provider, Map.entry(providerTransportMap.get(provider), usedTransportPoints.get(provider)));
        }

        return providersWithTransportAndCapacity;
    }
}