package com.github.alllef.transportationservice.ui.transport_point.entity_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public abstract class TransportPointLayout<T extends TransportPoint> extends VerticalLayout {
    @Getter
    private final T transportPoint;
    private int maxCapacity;

    private Text nameAddressText;
    protected NumberField capacityField = new NumberField();
    private Button deleteButton = new Button("Delete");

    public TransportPointLayout(T transportPoint) {
        this.transportPoint = transportPoint;
        nameAddressText = new Text(String.format("""
                City: %s
                Address: %s""",transportPoint.getName(),transportPoint.getAddress()));
        add(nameAddressText, capacityField, deleteButton);
        configureCapacityField();
        configureButton();
        configureLayoutStyle();
    }

    private void configureLayoutStyle() {
        getStyle().set("border", "1px solid black");
    }

    protected void configureCapacityField() {
        if (transportPoint instanceof Consumer consumer)
            capacityField.setMax(consumer.getMaxNeeds());
        else if (transportPoint instanceof Provider provider)
            capacityField.setMax(provider.getMaxCapacity());

        capacityField.setMin(1);
        capacityField.setValue(1.0);
        capacityField.setHasControls(true);
        capacityField.setSuffixComponent(new Span("tons"));
        capacityField.setStep(1);
        capacityField.addValueChangeListener(event -> {
            this.maxCapacity = event.getValue()
                    .intValue();
            fireEvent(new TransportPointEvent.CapacityChangedEvent(this, transportPoint, maxCapacity));
        });
    }

    private void configureButton() {
        deleteButton.addClickListener(buttonClickEvent ->
                fireEvent(new TransportPointEvent.DeleteEvent(this, transportPoint)));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
