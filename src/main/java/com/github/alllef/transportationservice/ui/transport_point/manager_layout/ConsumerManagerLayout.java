package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.vaadin.flow.component.ClickEvent;

public class ConsumerManagerLayout extends TransportPointManagerLayout<Consumer> {
    private final ConsumerService consumerService;

    public ConsumerManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory, ConsumerService consumerService) {
        super(transportPointLayoutFactory);
        this.consumerService = consumerService;
        choosePointComboBox.setItems(consumerService.findAll());
    }

    @Override
    protected void configureChoosePointComboBox() {
        choosePointComboBox.setLabel("Choose consumer");
        choosePointComboBox.setItemLabelGenerator(item -> String.format("%s(max needs: %s)", item.getName(), item.getMaxNeeds()));
    }

    @Override
    protected void onButtonClicked(ClickEvent<?> clickEvent) {
        super.onButtonClicked(clickEvent);
        resetComboBoxValues(consumerService.findAll());
    }

    @Override
    protected void onDeleteEvent(TransportPointEvent.DeleteEvent deleteEvent) {
        if (deleteEvent.getTransportPoint() instanceof Consumer consumer) {
            usedTransportPoints.remove(consumer);
            fireEvent(new TransportPointManagerEvent.ConsumerDeleteEvent(this, consumer));
            resetComboBoxValues(consumerService.findAll());
        }
    }

}
