package com.github.alllef.transportationservice.ui.transport_point.form_layout.layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ConsumerFormEvent;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ProviderFormEvent;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class ConsumerFormLayout extends TransportPointFormLayout {
    private Consumer consumer = new Consumer();
    private NumberField maxNeeds = new NumberField("Max needs");
    private Binder<Consumer> binder = new BeanValidationBinder<>(Consumer.class);

    public ConsumerFormLayout() {
        add(maxNeeds, createButtonsLayout());
        configureBinder();
    }

    @Override
    protected void configureButtons() {
        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new ConsumerFormEvent.ConsumerFormCloseEvent(this)));
    }

    @Override
    protected void configureBinder() {
        binder.forField(maxNeeds)
                .withConverter(new DoubleToIntConverter())
                .bind("maxNeeds");
        binder.bindInstanceFields(this);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(consumer);
            fireEvent(new ConsumerFormEvent.ConsumerSaveEvent(this, consumer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}