package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayout;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;


public abstract class TransportPointManagerLayout<T extends TransportPoint> extends VerticalLayout {
    private final TransportPointLayoutFactory transportPointLayoutFactory;

    @Getter
    protected Map<T, Integer> usedTransportPoints = new HashMap<>();

    protected ComboBox<T> choosePointComboBox = new ComboBox<>();
    private Button addButton = new Button("Add");

    public TransportPointManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory) {
        this.transportPointLayoutFactory = transportPointLayoutFactory;
        add(choosePointComboBox, addButton);
        configureAddButton();
        configureChoosePointComboBox();
        setSizeFull();
    }

    protected abstract void configureChoosePointComboBox();

    protected void configureAddButton() {
        addButton.addClickListener(this::onButtonClicked);
        addButton.setIcon(VaadinIcon.PLUS.create());
    }

    protected void onButtonClicked(ClickEvent<?> clickEvent) {
        T currentValue = choosePointComboBox.getValue();
        TransportPointLayout<?> transportPointLayout = transportPointLayoutFactory.createTransportLayout(currentValue);

        transportPointLayout.addListener(TransportPointEvent.DeleteEvent.class,
                deleteEvent -> {
                    this.remove(transportPointLayout);
                    onDeleteEvent(deleteEvent);
                });

        transportPointLayout.addListener(TransportPointEvent.ProviderConfiguredEvent.class,
                configuredEvent -> {
                    fireEvent(new TransportPointManagerEvent.ProviderAddEvent(this, configuredEvent.getProvider(), configuredEvent.getTransport()));
                    onProviderConfiguredEvent(configuredEvent);
                });

        transportPointLayout.addListener(TransportPointEvent.CapacityChangedEvent.class,
                event -> {
                    T tmpTransportPoint = (T) event.getTransportPoint();
                    if (tmpTransportPoint instanceof Provider tmpProvider)
                        fireEvent(new TransportPointManagerEvent.ProviderCapacityChangedEvent(this, tmpProvider, event.getCapacity()));
                    else if (tmpTransportPoint instanceof Consumer tmpConsumer)
                        fireEvent(new TransportPointManagerEvent.ConsumerCapacityChangedEvent(this, tmpConsumer, event.getCapacity()));
                    usedTransportPoints.put(tmpTransportPoint, event.getCapacity());
                });

        usedTransportPoints.put(currentValue, 1);
        add(transportPointLayout);

        if (currentValue instanceof Consumer consumer)
            fireEvent(new TransportPointManagerEvent.ConsumerAddEvent(this, consumer));
    }

    protected void resetComboBoxValues(List<T> allValues) {
        List<T> values = allValues
                .stream()
                .filter(value -> !usedTransportPoints.containsKey(value))
                .collect(Collectors.toList());
        choosePointComboBox.setItems(values);
    }

    @Override
    public <S extends ComponentEvent<?>> Registration addListener(Class<S> eventType, ComponentEventListener<S> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    protected void onProviderConfiguredEvent(TransportPointEvent.ProviderConfiguredEvent event) {
    }

    protected abstract void onDeleteEvent(TransportPointEvent.DeleteEvent deleteEvent);
}