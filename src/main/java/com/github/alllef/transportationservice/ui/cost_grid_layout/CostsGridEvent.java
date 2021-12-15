package com.github.alllef.transportationservice.ui.cost_grid_layout;

import com.vaadin.flow.component.ComponentEvent;

public abstract class CostsGridEvent extends ComponentEvent<CostsGridLayout> {

    public CostsGridEvent(CostsGridLayout source) {
        super(source, false);
    }

    public static class CalculationEvent extends CostsGridEvent{
        CalculationEvent(CostsGridLayout source){
            super(source);
        }
    }

}