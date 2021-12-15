package com.github.alllef.transportationservice.ui.grid_layout;

import com.vaadin.flow.component.ComponentEvent;

public abstract class CostsGridEvent extends ComponentEvent<GridLayout> {

    public CostsGridEvent(GridLayout source) {
        super(source, false);
    }

    public static class CalculationEvent extends CostsGridEvent{
        CalculationEvent(GridLayout source){
            super(source);
        }
    }

}