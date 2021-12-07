package com.github.alllef.transportationservice.ui.transport_point;

import com.vaadin.flow.component.ComponentEvent;

public abstract class TransportPointEvent extends ComponentEvent<TransportPointLayout<?>> {

    public TransportPointEvent(TransportPointLayout<?> source) {
        super(source, false);
    }

    public static class DeleteEvent extends TransportPointEvent {
        public DeleteEvent(TransportPointLayout<?> source) {
            super(source);
        }
    }
}
