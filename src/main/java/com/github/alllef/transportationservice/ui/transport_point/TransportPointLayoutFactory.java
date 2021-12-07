package com.github.alllef.transportationservice.ui.transport_point;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.service.ConsumerService;
import com.github.alllef.transportationservice.backend.database.service.ProviderService;
import com.github.alllef.transportationservice.backend.database.service.TransportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransportPointLayoutFactory {
    private final TransportService transportService;

    public ConsumerLayout createConsumerLayout(Consumer consumer) {
        return new ConsumerLayout(consumer);
    }

    public ProviderLayout createProviderLayout(Provider provider) {
        return new ProviderLayout(provider,transportService);
    }
}
