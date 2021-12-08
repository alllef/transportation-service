package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import com.vaadin.flow.component.ClickEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsumerManagerLayout extends TransportPointManagerLayout<Consumer> {
    private final ConsumerService consumerService;

    public ConsumerManagerLayout(TransportPointLayoutFactory transportPointLayoutFactory, ConsumerService consumerService) {
        super(transportPointLayoutFactory);
        this.consumerService = consumerService;
        choosePointComboBox.setItems(consumerService.findAll());
    }

    @Override
    protected void onButtonClicked(ClickEvent<?> clickEvent) {
        super.onButtonClicked(clickEvent);
        List<Consumer> values = consumerService.findAll()
                .stream()
                .filter(value -> !usedTransportPoints.contains(value))
                .collect(Collectors.toList());

        choosePointComboBox.setItems(values);
    }

}
