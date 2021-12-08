package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayoutFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransportPointManagerLayoutFactory {
    private final ProviderService providerService;
    private final ConsumerService consumerService;
    private final TransportPointLayoutFactory transportPointLayoutFactory;

    public ProviderManagerLayout createProviderManagerLayout(){
        return new ProviderManagerLayout(transportPointLayoutFactory,providerService);
    }

    public ConsumerManagerLayout createConsumerManagerLayout(){
        return new ConsumerManagerLayout(transportPointLayoutFactory,consumerService);
    }

}
