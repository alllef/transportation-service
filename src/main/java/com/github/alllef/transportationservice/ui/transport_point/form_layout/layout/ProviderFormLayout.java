package com.github.alllef.transportationservice.ui.transport_point.form_layout.layout;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ProviderFormEvent;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class ProviderFormLayout extends TransportPointFormLayout {
    private Provider provider = new Provider();
    private NumberField maxCapacity = new NumberField("Max capacity");
    private Binder<Provider> binder = new BeanValidationBinder<>(Provider.class);

    public ProviderFormLayout() {
        add(maxCapacity, createButtonsLayout());
        configureBinder();
    }

    @Override
    protected void configureButtons() {
        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new ProviderFormEvent.ProviderFormCloseEvent(this)));
    }

    @Override
    protected void configureBinder() {
        binder.forField(maxCapacity)
                .withConverter(new DoubleToIntConverter())
                .bind("maxCapacity");
        binder.bindInstanceFields(this);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(provider);
            fireEvent(new ProviderFormEvent.ProviderSaveEvent(this, provider));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}