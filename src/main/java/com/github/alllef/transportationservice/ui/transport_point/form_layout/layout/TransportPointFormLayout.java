package com.github.alllef.transportationservice.ui.transport_point.form_layout.layout;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;

public abstract class TransportPointFormLayout extends FormLayout {
    protected TextField name = new TextField("Name");
    protected TextField address = new TextField("Address");
    protected Button save = new Button("Save");
    protected Button close = new Button("Close");

    TransportPointFormLayout() {
        add(name, address);
        configureButtons();
    }

    protected HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(save, close);
    }

    protected abstract void configureButtons();

    protected abstract void configureBinder();

    protected abstract void configureNumberField();

    @Override
    public <S extends ComponentEvent<?>> Registration addListener(Class<S> eventType, ComponentEventListener<S> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    protected static class DoubleToIntConverter implements Converter<Double, Integer> {

        @Override
        public Result<Integer> convertToModel(Double aDouble, ValueContext valueContext) {
            return Result.ok(aDouble.intValue());
        }

        @Override
        public Double convertToPresentation(Integer integer, ValueContext valueContext) {
            return (double) integer;
        }
    }
}