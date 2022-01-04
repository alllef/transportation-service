package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.Data;

import java.util.Map;

@Data
public class TransportAlgo {
    private final CostsModel costsModel;
    private Map<Coords, Integer> nodesWithShipments;
    private int[][] tmpCostsMatrix;

    public void startAlgo() {
        MinCostMethod minCostMethod = new MinCostMethod(costsModel);
        minCostMethod.startAlgo();
        PotentialsMethod potentials = new PotentialsMethod(minCostMethod.getCostsModel(),minCostMethod.getNodesWithPlanNum());
        nodesWithShipments = potentials.getPotentialNodes();
        tmpCostsMatrix = minCostMethod.getCostsModel().getCostsMatrix();
    }

    public int getTransportationPrice() {
        return AlgoUtils.calcTransportSum(nodesWithShipments, tmpCostsMatrix);
    }
}
