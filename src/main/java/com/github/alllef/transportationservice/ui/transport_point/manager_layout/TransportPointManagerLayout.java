package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.ConsumerLayout;
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
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;


public class TransportPointManagerLayout<T extends TransportPoint> extends VerticalLayout {
    private final TransportPointLayoutFactory transportPointLayoutFactory;

    @Getter
    protected Set<T> usedTransportPoints = new HashSet<>();

    protected ComboBox<T> choosePointComboBox = new ComboBox<>("Choose entity");
    private Button addButton = new Button("Add");

    public TransportPointManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory) {
        this.transportPointLayoutFactory = transportPointLayoutFactory;
        add(choosePointComboBox, addButton);
        configureAddButton();
        configureChoosePointComboBox();
    }

    protected void configureChoosePointComboBox() {
        choosePointComboBox.setItemLabelGenerator(T::getName);
    }

    protected void configureAddButton() {
        addButton.addClickListener(this::onButtonClicked);
        addButton.setIcon(VaadinIcon.PLUS.create());
    }

    protected void onButtonClicked(ClickEvent<?> clickEvent) {
        T currentValue = choosePointComboBox.getValue();
        TransportPointLayout<?> transportPointLayout = transportPointLayoutFactory.createTransportLayout(currentValue);

        transportPointLayout.addListener(TransportPointEvent.DeleteEvent.class,
                deleteEvent -> this.remove(transportPointLayout));
        usedTransportPoints.add(currentValue);
        add(transportPointLayout);

        if(currentValue instanceof Consumer consumer)
            fireEvent(new TransportPointManagerEvent.ConsumerAddEvent(this,consumer));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
