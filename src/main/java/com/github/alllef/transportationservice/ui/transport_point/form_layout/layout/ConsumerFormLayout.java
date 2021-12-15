package com.github.alllef.transportationservice.ui.transport_point.form_layout.layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.ui.transport_point.form_layout.event.ConsumerFormEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

public class ConsumerFormLayout extends TransportPointFormLayout {
    private Consumer consumer = new Consumer();
    private NumberField maxNeeds = new NumberField("Max needs");
    private Binder<Consumer> binder = new BeanValidationBinder<>(Consumer.class);

    public ConsumerFormLayout() {
        add(maxNeeds, createButtonsLayout());
        configureBinder();
        configureNumberField();
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

    @Override
    protected void configureNumberField() {
        maxNeeds.setMax(1000000);

        maxNeeds.setMin(1);
        maxNeeds.setValue(1.0);
        maxNeeds.setHasControls(true);
        maxNeeds.setSuffixComponent(new Span("tons"));
        maxNeeds.setStep(1);
        maxNeeds.setHelperText("Max value is: "+maxNeeds.getMax());
    }

    private void validateAndSave() {
        try {
            binder.writeBean(consumer);
            fireEvent(new ConsumerFormEvent.ConsumerSaveEvent(this, consumer));
        } catch (Exception e) {
            Notification notification = new Notification("Incorrect values in consumer form");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setDuration(5000);
            notification.open();
            e.printStackTrace();
        }
    }
}