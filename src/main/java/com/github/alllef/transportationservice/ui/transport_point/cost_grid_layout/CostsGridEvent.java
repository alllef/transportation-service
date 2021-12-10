package com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.TransportPointManagerEvent;
import com.github.alllef.transportationservice.ui.transport_point.manager_layout.TransportPointManagerLayout;
import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;

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