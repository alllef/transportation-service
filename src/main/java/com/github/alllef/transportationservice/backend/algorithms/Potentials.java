package com.github.alllef.transportationservice.backend.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Potentials {
    private MinCostMethod minCostMethod;
    private Map<Integer, CostNode> nodesWithPlanNum = new HashMap<>();
    List<Optional<Integer>> stocksPotentials;
    List<Optional<Integer>> needsPotentials;

    public void calcPotentials() {
        int defaultStockPotential = 0;
        minCostMethod.getCostsModel().getCostsMatrix()
    }
}
