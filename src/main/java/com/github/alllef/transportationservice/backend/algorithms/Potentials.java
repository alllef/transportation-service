package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Potentials {
    private MinCostMethod minCostMethod;
    private Map<Coords, Integer> potentialNodes = new HashMap<>();
    List<Optional<Integer>> providersPotentials;
    List<Optional<Integer>> consumerPotentials;

    Potentials() {
        potentialNodes.putAll(minCostMethod.getNodesWithPlanNum());
        for (int i = 0; i < minCostMethod.getTmpConsumers().size(); i++)
            providersPotentials.add(Optional.empty());

        for (int i = 0; i < minCostMethod.getTmpProviders().size(); i++)
            consumerPotentials.add(Optional.empty());
    }

    public void calcPotentials() {
        providersPotentials.set(0, Optional.of(0));
        Map<Coords, Integer> costs = minCostMethod.getCostsModel().getCostsMatrix();
        while (providersPotentials.contains(Optional.empty()) || consumerPotentials.contains(Optional.empty())) {

        }
    }

}
