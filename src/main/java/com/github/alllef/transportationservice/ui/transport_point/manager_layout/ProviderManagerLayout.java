package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.vaadin.flow.component.ClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ProviderManagerLayout extends TransportPointManagerLayout<Provider> {
    private final ProviderService providerService;

    public ProviderManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory, ProviderService providerService) {
        super(transportPointLayoutFactory);
        this.providerService = providerService;
        choosePointComboBox.setItems(providerService.findAll());
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
}
