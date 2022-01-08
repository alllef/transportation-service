package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.*;

import java.util.Map;

@EqualsAndHashCode
@ToString
@Getter
public class TransportAlgo {
    private CostsModel costsModel;
    private Map<Coords, Integer> nodesWithShipments;

    public TransportAlgo(CostsModel costsModel) {
        this.costsModel = costsModel;
    }

    public void startAlgo() {
        MinCostMethod minCostMethod = new MinCostMethod(costsModel);
        minCostMethod.startAlgo();
        PotentialsMethod potentials = new PotentialsMethod(minCostMethod.getCostsModel(), minCostMethod.getNodesWithPlanNum());
        nodesWithShipments = potentials.getPotentialNodes();
        costsModel = potentials.getCostsModel();
    }

    public int getTransportationPrice() {
        return AlgoUtils.calcTransportSum(nodesWithShipments, costsModel.getCostsMatrix());
    }
}
