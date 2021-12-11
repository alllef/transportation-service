package com.github.alllef.transportationservice.ui.transport_point.entity_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;

public class ConsumerLayout extends TransportPointLayout<Consumer> {

    public ConsumerLayout(Consumer transportPoint) {
        super(transportPoint);
    }

    public Consumer getConsumer(){
        return getTransportPoint();
    }

    @Override
    protected void configureCapacityField() {
        super.configureCapacityField();
        int maxNeeds = getConsumer()
                .getMaxNeeds();

        capacityField.setMax(maxNeeds);
        capacityField.setHelperText("Max needs are: " + maxNeeds);
        capacityField.setLabel("Needs");
    }
}
