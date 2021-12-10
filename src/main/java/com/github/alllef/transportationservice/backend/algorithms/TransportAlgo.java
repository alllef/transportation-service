package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.Data;

import java.util.Map;

@Data
public class TransportAlgo {
    private final CostsModel costsModel;
    private Map<Coords, Integer> nodesWithShipments;

    public void startAlgo() {
        MinCostMethod minCostMethod = new MinCostMethod(costsModel);
        Potentials potentials = new Potentials(minCostMethod);
        nodesWithShipments = potentials.getPotentialNodes();
    }

    public int getTransportationPrice() {
        return AlgoUtils.calcTransportSum(nodesWithShipments, potentials.getMinCostMethod().getTmpCostsMatrix());
    }

}
