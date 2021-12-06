package com.github.alllef.transportationservice.ui;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.Optional;

public class TransportPointLayout<T extends TransportPoint> extends VerticalLayout {
    private final T transportPoint;

    private Label nameLabel;
    private Label addressLabel;
    private NumberField capacityField = new NumberField("Max capacity");
    private Button deleteButton = new Button("Delete");

    TransportPointLayout(T transportPoint) {
        this.transportPoint = transportPoint;
        nameLabel = new Label(transportPoint.getName());
        addressLabel = new Label(transportPoint.getAddress());
        configureCapacityField();
    }

    private void configureCapacityField() {
        capacityField.setMax(provider.getMaxCapacity());
        capacityField.setMin(1);
        capacityField.setHasControls(true);
        capacityField.setSuffixComponent(new Span("tons"));
        capacityField.setStep(1);
    }

    private void configureButton() {
        deleteButton.addClickListener(buttonClickEvent -> {
            Optional<Component> parent = getParent();
            if (parent.isPresent())

        })
    }
}
