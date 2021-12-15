package com.github.alllef.transportationservice.ui.transport_point.form_layout.event;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.layout.ProviderFormLayout;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

public class ProviderFormEvent extends ComponentEvent<ProviderFormLayout> {

    public ProviderFormEvent(ProviderFormLayout source) {
        super(source, false);
    }

    public static class ProviderSaveEvent extends ProviderFormEvent {
        @Getter
        private final Provider provider;

        public ProviderSaveEvent(ProviderFormLayout source, Provider provider) {
            super(source);
            this.provider=provider;
        }
    }

    public static class ProviderFormCloseEvent extends ProviderFormEvent {

        public ProviderFormCloseEvent(ProviderFormLayout source) {
            super(source);
        }
    }

}
