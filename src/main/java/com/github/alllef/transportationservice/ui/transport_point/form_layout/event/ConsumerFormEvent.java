package com.github.alllef.transportationservice.ui.transport_point.form_layout.event;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.layout.ConsumerFormLayout;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.layout.ProviderFormLayout;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

public class ConsumerFormEvent extends ComponentEvent<ConsumerFormLayout> {

    public ConsumerFormEvent(ConsumerFormLayout source) {
        super(source, false);
    }

    public static class ConsumerSaveEvent extends ConsumerFormEvent {
        @Getter
        private final Consumer consumer;

        public ConsumerSaveEvent(ConsumerFormLayout source, Consumer consumer) {
            super(source);
            this.consumer = consumer;
        }
    }

    public static class ConsumerFormCloseEvent extends ConsumerFormEvent {

        public ConsumerFormCloseEvent(ConsumerFormLayout source) {
            super(source);
        }
    }

}