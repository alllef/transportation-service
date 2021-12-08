package com.github.alllef.transportationservice.ui.transport_point.entity_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.ConsumerLayout;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.ProviderLayout;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransportPointLayoutFactory {
    private final TransportService transportService;

    public TransportPointLayout<?> createTransportLayout(TransportPoint transportPoint) {
        if (transportPoint instanceof Consumer consumer)
            return createConsumerLayout(consumer);
        else if (transportPoint instanceof Provider provider)
            return createProviderLayout(provider);
        return null;
    }

    private ConsumerLayout createConsumerLayout(Consumer consumer) {
        return new ConsumerLayout(consumer);
    }

    private ProviderLayout createProviderLayout(Provider provider) {
        return new ProviderLayout(provider, transportService);
    }
}
