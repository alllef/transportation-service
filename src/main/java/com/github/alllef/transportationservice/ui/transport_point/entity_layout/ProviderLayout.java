package com.github.alllef.transportationservice.ui.transport_point.entity_layout;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.ProviderManagerLayout;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.TransportPointManagerEvent;
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

    @Override
    protected void configureCapacityField() {
        super.configureCapacityField();
        int maxCapacity = getProvider()
                .getMaxCapacity();

        capacityField.setMax(maxCapacity);
        capacityField.setHelperText("Max capacity is: " + maxCapacity);
        capacityField.setLabel("Capacity");
    }

    private void configureTransportComboBox() {
        transportComboBox.setItems(transportService.findAll());
        transportComboBox.setPlaceholder("Choose transport");
        transportComboBox.setHelperText("You haven't choose transport yet");
        transportComboBox.setClearButtonVisible(true);
        transportComboBox.setItemLabelGenerator(transport ->
                transport.getName() + " " + transport.getFuelConsumptionPerKm() + "liters/km");
        transportComboBox.addValueChangeListener(event -> {
            transportLabel.setText(event.getValue()
                    .getName());
            transportComboBox.setHelperText("");
            fireEvent(new TransportPointEvent.ProviderConfiguredEvent(this, getTransport(), getProvider()));
        });
    }

    public Transport getTransport() {
        return transportComboBox.getValue();
    }

    public Provider getProvider() {
        return getTransportPoint();
    }
}
