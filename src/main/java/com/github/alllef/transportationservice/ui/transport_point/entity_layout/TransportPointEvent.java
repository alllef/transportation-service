package com.github.alllef.transportationservice.ui.transport_point.entity_layout;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.github.alllef.transportationservice.backend.database.entity.TransportPoint;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

public abstract class TransportPointEvent extends ComponentEvent<TransportPointLayout<?>> {

    public TransportPointEvent(TransportPointLayout<?> source) {
        super(source, false);
    }

    public static class DeleteEvent extends TransportPointEvent {
        @Getter
        TransportPoint transportPoint;

        public DeleteEvent(TransportPointLayout<?> source,TransportPoint transportPoint) {
            super(source);
            this.transportPoint = transportPoint;
        }
    }

    public static class ProviderConfiguredEvent extends TransportPointEvent {
        @Getter
        private Transport transport;
        @Getter
        private Provider provider;

        public ProviderConfiguredEvent(TransportPointLayout<?> source, Transport transport, Provider provider) {
            super(source);
            this.transport = transport;
            this.provider = provider;
        }
    }

}
