package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.github.alllef.transportationservice.backend.algorithms.utils.enums.EntityType;
import com.helger.commons.collection.map.MapEntry;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

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
        while (pathCost.isPresent()) {
            optimizeSolution(getPathsCycle(pathCost.get()));
            pathCost = isOptimalSolution();
        }
    }

    private void calcPotentials() {
        providersPotentials.clear();
        consumersPotentials.clear();

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

    private Optional<Map.Entry<Coords, Integer>> isOptimalSolution() {
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

        return Optional.ofNullable(minUnusedPathCost);
    }

    private List<Coords> getPathsCycle(Map.Entry<Coords, Integer> starterNode) {
        potentialNodes.put(starterNode.getKey(), 0);

        Coords start = starterNode.getKey();
        Set<Coords> foundCoords = new HashSet<>();
        Stack<Coords> planNodeCoordsStack = new Stack<>();
        Coords coordsFromStart = null;

        Map<Coords, List<Coords>> nearestCoordsGraph = new CycleGraph(potentialNodes.keySet(), providersPotentials.size(),
                consumersPotentials.size())
                .getNearestCoords();

        foundCoords.add(start);
        planNodeCoordsStack.push(start);
        boolean isFoundFinal = false;

        do {
            Coords peekCoords = planNodeCoordsStack.peek();
            boolean foundNext = false;

            for (Coords tmpCoords : nearestCoordsGraph.get(peekCoords)) {
                if (!foundCoords.contains(tmpCoords)) {
                    if (peekCoords.equals(start))
                        coordsFromStart = tmpCoords;

                    foundCoords.add(tmpCoords);
                    planNodeCoordsStack.push(tmpCoords);
                    foundNext = true;
                    break;
                } else if (tmpCoords.equals(start) && !peekCoords.equals(coordsFromStart)) {
                    isFoundFinal = true;
                    break;
                }
            }

            if (!foundNext && !isFoundFinal)
                planNodeCoordsStack.pop();

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

        if (AlgoUtils.isDegenerate(providersPotentials.size(), consumersPotentials.size(), potentialNodes.size())) {
            potentialNodes = potentialNodes.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

}