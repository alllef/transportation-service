package com.github.alllef.transportationservice.ui.transport_point;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.util.Optional;

public abstract class TransportPointLayout<T extends TransportPoint> extends VerticalLayout {
    @Getter
    private final T transportPoint;

    private Label nameLabel;
    private Label addressLabel;
    private NumberField capacityField = new NumberField("Max capacity");
    private Button deleteButton = new Button("Delete");

    public TransportPointLayout(T transportPoint) {
        this.transportPoint = transportPoint;
        nameLabel = new Label(transportPoint.getName());
        addressLabel = new Label(transportPoint.getAddress());
        add(nameLabel, addressLabel, capacityField, deleteButton);
        configureCapacityField();
        configureButton();
    }

    private void configureCapacityField() {
        if (transportPoint instanceof Consumer consumer)
            capacityField.setMax(consumer.getMaxNeeds());
        else if (transportPoint instanceof Provider provider)
            capacityField.setMax(provider.getMaxCapacity());

        capacityField.setMin(1);
        capacityField.setHasControls(true);
        capacityField.setSuffixComponent(new Span("tons"));
        capacityField.setStep(1);
    }

    private void configureButton() {
        deleteButton.addClickListener(buttonClickEvent ->
                fireEvent(new TransportPointEvent.DeleteEvent(this)));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
