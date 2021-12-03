package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.enums.Coords;
import com.helger.commons.collection.map.MapEntry;
import lombok.Getter;

import java.util.*;

@Getter
public class Potentials {
    private MinCostMethod minCostMethod;
    private Map<Coords, Integer> potentialNodes = new HashMap<>();
    private Map<Integer, Integer> providersPotentials = new HashMap<>();
    private Map<Integer, Integer> consumersPotentials = new HashMap<>();

    public Potentials(MinCostMethod minCostMethod) {
        this.minCostMethod = minCostMethod;
        potentialNodes.putAll(minCostMethod.getNodesWithPlanNum());

        potentialsAlgo();
    }

    private void potentialsAlgo() {
        var pathCost = isOptimalSolution();
        while (isOptimalSolution().isPresent())
            optimizeSolution(getPathsCycle(pathCost.get()));
    }

    private void calcPotentials() {
        providersPotentials.put(1, 0);
        Map<Coords, Integer> costs = minCostMethod.getTmpCostsMatrix();
        while (providersPotentials.size() < minCostMethod.getTmpProviders().size() || consumersPotentials.size() < minCostMethod.getTmpConsumers().size()) {
            for (Coords coords : potentialNodes.keySet()) {
                if (providersPotentials.get(coords.provider()) != null && consumersPotentials.get(coords.consumer()) == null)
                    consumersPotentials.put(coords.consumer(), costs.get(coords) - providersPotentials.get(coords.provider()));
                else if (providersPotentials.get(coords.provider()) == null && consumersPotentials.get(coords.consumer()) != null)
                    providersPotentials.put(coords.provider(), costs.get(coords) - consumersPotentials.get(coords.consumer()));
            }
        }
    }

    public Optional<Map.Entry<Coords, Integer>> isOptimalSolution() {
        this.calcPotentials();
        Map<Coords, Integer> costs = minCostMethod.getCostsModel().getCostsMatrix();
        Map<Coords, Integer> nodesWithPlanNum = minCostMethod.getNodesWithPlanNum();
        Map.Entry<Coords, Integer> minUnusedPathCost = null;

        boolean isOptimalSolution = true;

        for (Coords key : costs.keySet()) {
            if (nodesWithPlanNum.get(key) == null) {
                int pathCost = costs.get(key) - (providersPotentials.get(key.provider()) + consumersPotentials.get(key.consumer()));
                if (pathCost < 0) {
                    if (isOptimalSolution) {
                        minUnusedPathCost = new MapEntry<>(key, pathCost);
                        isOptimalSolution = false;
                    } else if (pathCost < minUnusedPathCost.getValue())
                        minUnusedPathCost = new MapEntry<>(key, pathCost);
                }
            }
        }
        if (minUnusedPathCost != null)
            potentialNodes.put(minUnusedPathCost.getKey(), minUnusedPathCost.getValue());

        return Optional.ofNullable(minUnusedPathCost);
    }

    private List<Coords> getPathsCycle(Map.Entry<Coords, Integer> starterNode) {
        Set<Coords> foundCoords = new HashSet<>();
        Stack<Coords> planNodeCoordsStack = new Stack<>();
        planNodeCoordsStack.push(starterNode.getKey());
        foundCoords.add(starterNode.getKey());
        boolean isFoundFinal = false;

        do {
            Coords coords = planNodeCoordsStack.peek();

            for (int providerKey = 0; providerKey < providersPotentials.size(); providerKey++) {
                Coords tmpCoords = new Coords(providerKey, coords.consumer());
                if (potentialNodes.get(tmpCoords) != null) {
                    if (!foundCoords.contains(tmpCoords)) {
                        foundCoords.add(coords);
                        planNodeCoordsStack.push(tmpCoords);
                        break;
                    } else if (tmpCoords.equals(starterNode.getKey())) {
                        isFoundFinal = true;
                        break;
                    }
                }
            }

            if (isFoundFinal)
                break;

            for (int consumerKey = 0; consumerKey < consumersPotentials.size(); consumerKey++) {
                Coords tmpCoords = new Coords(coords.provider(), consumerKey);
                if (potentialNodes.get(tmpCoords) != null) {
                    if (!foundCoords.contains(tmpCoords)) {
                        foundCoords.add(coords);
                        planNodeCoordsStack.push(tmpCoords);
                        break;
                    } else if (tmpCoords.equals(starterNode.getKey())) {
                        isFoundFinal = true;
                        break;
                    }
                }
            }

        } while (!isFoundFinal);

        return planNodeCoordsStack.stream()
                .toList();
    }

    private void optimizeSolution(List<Coords> pathsCycle) {
        List<Coords> minusCoords = new ArrayList<>();

        for (int i = 1; i < pathsCycle.size(); i += 2)
            minusCoords.add(pathsCycle.get(i));

        int changeNumber = minusCoords.stream()
                .map(potentialNodes::get)
                .min(Integer::compare)
                .orElseThrow();

        for (int i = 0; i < pathsCycle.size(); i++) {
            if (i % 2 == 0) potentialNodes.put(pathsCycle.get(i), potentialNodes.get(pathsCycle.get(i)) + changeNumber);
            else
                potentialNodes.put(pathsCycle.get(i), potentialNodes.get(pathsCycle.get(i)) - changeNumber);
        }
    }

}