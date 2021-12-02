package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.helger.commons.collection.map.MapEntry;

import java.util.*;

public class Potentials {
    private MinCostMethod minCostMethod;
    private Map<Coords, Integer> potentialNodes = new HashMap<>();
    private List<Optional<Integer>> providersPotentials;
    private List<Optional<Integer>> consumersPotentials;

    Potentials() {
        potentialNodes.putAll(minCostMethod.getNodesWithPlanNum());
        for (int i = 0; i < minCostMethod.getTmpConsumers().size(); i++)
            providersPotentials.add(Optional.empty());

        for (int i = 0; i < minCostMethod.getTmpProviders().size(); i++)
            consumersPotentials.add(Optional.empty());
    }

    public void calcPotentials() {
        providersPotentials.set(0, Optional.of(0));
        Map<Coords, Integer> costs = minCostMethod.getCostsModel().getCostsMatrix();
        while (providersPotentials.contains(Optional.empty()) || consumersPotentials.contains(Optional.empty())) {
            for (Coords coords : potentialNodes.keySet()) {
                if (providersPotentials.get(coords.provider()).isPresent() && consumersPotentials.get(coords.consumer()).isEmpty())
                    consumersPotentials.set(coords.consumer(), Optional.of(costs.get(coords) - providersPotentials.get(coords.provider()).get()));
                else if (providersPotentials.get(coords.provider()).isEmpty() && consumersPotentials.get(coords.consumer()).isPresent())
                    providersPotentials.set(coords.consumer(), Optional.of(costs.get(coords) - consumersPotentials.get(coords.provider()).get()));
            }

        }
    }

    public boolean isOptimalSolution() {
        Map<Coords, Integer> costs = minCostMethod.getCostsModel().getCostsMatrix();
        Map<Coords, Integer> nodesWithPlanNum = minCostMethod.getNodesWithPlanNum();
        Map.Entry<Coords, Integer> minUnusedPathCost = null;

        boolean isOptimalSolution = true;

        for (Coords key : costs.keySet()) {
            if (nodesWithPlanNum.get(key) == null) {
                int pathCost = costs.get(key) - (providersPotentials.get(key.provider()).get() + consumersPotentials.get(key.consumer()).get());
                if (pathCost < 0) {
                    if (isOptimalSolution) {
                        minUnusedPathCost = new MapEntry<>(key, pathCost);
                        isOptimalSolution = false;
                    } else if (pathCost < minUnusedPathCost.getValue())
                        minUnusedPathCost = new MapEntry<>(key, pathCost);
                }
            }
        }
        if(minUnusedPathCost!=null)
            potentialNodes.put(minUnusedPathCost.getKey(),minUnusedPathCost.getValue());

        return isOptimalSolution;
    }

    public void optimizeSolution(Map.Entry<Coords,Integer> starterNode){
        Set<Coords> foundNodes;
        Stack<Map.Entry<Coords,Integer>> nodesWithPlanNum = new Stack<>();

    }
}