package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointEvent;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ConsumerFormEvent;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.layout.ConsumerFormLayout;
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
    protected void configureCreateButton() {
        createButton.setText("Create consumer");
        createButton.addClickListener(event -> {
            ConsumerFormLayout consumerFormLayout = new ConsumerFormLayout();
            addComponentAtIndex(3, consumerFormLayout);
            consumerFormLayout.addListener(ConsumerFormEvent.ConsumerSaveEvent.class, saveEvent -> {
                consumerService.save(saveEvent.getConsumer());
                remove(consumerFormLayout);
                resetComboBoxValues(consumerService.findAll());
            });
            consumerFormLayout.addListener(ConsumerFormEvent.ConsumerFormCloseEvent.class, closeEvent -> remove(consumerFormLayout));
        });
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
