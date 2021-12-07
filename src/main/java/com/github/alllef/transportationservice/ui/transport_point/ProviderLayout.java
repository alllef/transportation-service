package com.github.alllef.transportationservice.ui.transport_point;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;

public class ProviderLayout extends TransportPointLayout<Provider> {
    private final TransportService transportService;

    ComboBox<Transport> transportComboBox = new ComboBox<>("Transport");
    Label transportLabel = new Label();

    public ProviderLayout(Provider transportPoint, TransportService transportService) {
        super(transportPoint);
        this.transportService = transportService;
        add(transportComboBox, transportLabel);
        configureTransportComboBox();
    }

    private void configureTransportComboBox() {
        transportComboBox.setItems(transportService.findAll());
        transportComboBox.setPlaceholder("Find transport with different parameters");
        transportComboBox.setHelperText("You haven't choose transport yet");
        transportComboBox.addValueChangeListener(event -> transportLabel.setText(event.getValue()
                .getName()));
    }

    public Transport getTransport() {
        return transportComboBox.getValue();
    }

    public Provider getProvider() {
        return getTransportPoint();
    }
}
