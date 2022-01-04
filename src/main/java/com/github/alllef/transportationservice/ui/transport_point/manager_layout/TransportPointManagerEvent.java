package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

public abstract class TransportPointManagerEvent extends ComponentEvent<TransportPointManagerLayout<?>> {

    public TransportPointManagerEvent(TransportPointManagerLayout<?> source) {
        super(source, false);
    }

    public static class ConsumerAddEvent extends TransportPointManagerEvent {
        @Getter
        private final Consumer consumer;

        public ConsumerAddEvent(TransportPointManagerLayout<?> source, Consumer consumer) {
            super(source);
            this.consumer = consumer;
        }
    }

    @Getter
    public static class ProviderCapacityChangedEvent extends TransportPointManagerEvent {
        private final Provider provider;
        private final int capacity;

        public ProviderCapacityChangedEvent(TransportPointManagerLayout<?> source, Provider provider, int capacity) {
            super(source);
            this.provider = provider;
            this.capacity = capacity;
        }
    }

    @Getter
    public static class ConsumerCapacityChangedEvent extends TransportPointManagerEvent {
        private final Consumer consumer;
        private final int capacity;

        public ConsumerCapacityChangedEvent(TransportPointManagerLayout<?> source, Consumer consumer, int capacity) {
            super(source);
            this.consumer = consumer;
            this.capacity = capacity;
        }
    }

    public static class ConsumerDeleteEvent extends TransportPointManagerEvent {
        @Getter
        private final Consumer consumer;

        public ConsumerDeleteEvent(TransportPointManagerLayout<?> source, Consumer consumer) {
            super(source);
            this.consumer = consumer;
        }
    }

    @Getter
    public static class ProviderAddEvent extends TransportPointManagerEvent {
        private final Provider provider;
        private final Transport transport;

        public ProviderAddEvent(TransportPointManagerLayout<?> source, Provider provider, Transport transport) {
            super(source);
            this.provider = provider;
            this.transport = transport;
        }
    }

    public static class ProviderDeleteEvent extends TransportPointManagerEvent {
        @Getter
        private final Provider provider;

        public ProviderDeleteEvent(TransportPointManagerLayout<?> source, Provider provider) {
            super(source);
            this.provider = provider;
        }
    }
}
