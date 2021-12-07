package com.github.alllef.transportationservice.ui.transport_point;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;

public class ProviderLayout extends TransportPointLayout<Provider> {
    ComboBox<Transport> transportComboBox = new ComboBox<>("Transport");
    Label transportLabel = new Label("Your haven't choose transport yet. Please do this");

    public ProviderLayout(Provider transportPoint) {
        super(transportPoint);
        add(transportComboBox, transportLabel);
        configureTransportComboBox();
    }

    private void configureTransportComboBox() {
        transportComboBox.setPlaceholder("Find transport with different parameters");
        transportComboBox.addValueChangeListener(event -> transportLabel.setText(event.getValue()
                .getName()));
    }

    public Transport getTransport() {
        return transportComboBox.getValue();
    }

    public Provider getProvider(){
        return getTransportPoint();
    }
}
