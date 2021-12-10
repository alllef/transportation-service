package com.github.alllef.transportationservice.ui.transport_point.cost_grid_layout;

import com.github.alllef.transportationservice.backend.database.entity.Consumer;
import com.github.alllef.transportationservice.backend.database.entity.Provider;
import com.github.alllef.transportationservice.backend.database.entity.Transport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class CostsGridRow {
    private Provider provider;
    private Transport transport;
    private Map<Consumer,Integer> consumersWithTransportShipments;
}
