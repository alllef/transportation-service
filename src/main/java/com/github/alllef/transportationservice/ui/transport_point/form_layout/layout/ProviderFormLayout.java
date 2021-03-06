package com.github.alllef.transportationservice.ui.transport_point.form_layout.layout;

import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ProviderFormEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class ProviderFormLayout extends TransportPointFormLayout {
    private Provider provider = new Provider();
    private NumberField maxCapacity = new NumberField("Max capacity");
    private Binder<Provider> binder = new BeanValidationBinder<>(Provider.class);

    public ProviderFormLayout() {
        add(maxCapacity, createButtonsLayout());
        configureBinder();
        configureNumberField();
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

    @Override
    protected void configureNumberField() {
        maxCapacity.setMax(1000000);

        maxCapacity.setMin(1);
        maxCapacity.setValue(1.0);
        maxCapacity.setHasControls(true);
        maxCapacity.setSuffixComponent(new Span("tons"));
        maxCapacity.setStep(1);
        maxCapacity.setHelperText("Max value is: " + maxCapacity.getMax());
    }

    private void validateAndSave() {
        try {
            binder.writeBean(provider);
            fireEvent(new ProviderFormEvent.ProviderSaveEvent(this, provider));
        } catch (Exception e) {
            Notification notification = new Notification("Incorrect values in provider form");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();
            e.printStackTrace();
        }
    }
}