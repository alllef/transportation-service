package com.github.alllef.transportationservice.ui.transport_point.manager_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.entity_layout.TransportPointLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public abstract class TransportPointManagerEvent extends ComponentEvent<TransportPointManagerLayout<?>> {

    public TransportPointManagerEvent(TransportPointManagerLayout<?> source) {
        super(source, false);
    }

    public static class ConsumerAddEvent extends TransportPointManagerEvent {
        @Getter
        private Consumer consumer;

        public ConsumerAddEvent(TransportPointManagerLayout<?> source, Consumer consumer) {
            super(source);
            this.consumer = consumer;
        }
    }

    public static class ProviderAddEvent extends TransportPointManagerEvent {
        @Getter
        private Provider provider;

        public ProviderAddEvent(TransportPointManagerLayout<?> source, Provider provider) {
            super(source);
            this.provider = provider;
        }
    }

}
