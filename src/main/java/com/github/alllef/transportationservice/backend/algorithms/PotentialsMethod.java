package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.github.alllef.transportationservice.backend.algorithms.utils.CoordsNum;
import com.github.alllef.transportationservice.backend.algorithms.utils.enums.EntityType;
import com.helger.commons.collection.map.MapEntry;
import lombok.Getter;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class PotentialsMethod {
    private CostsModel costsModel;
    private Map<Coords, Integer> nodesWithPlanNum;

    private Map<Coords, Integer> potentialNodes = new HashMap<>();

    private static record Potentials(Map<Integer, Integer> providers,
                                     Map<Integer, Integer> consumers) {
    }

    public PotentialsMethod(CostsModel costsModel, Map<Coords, Integer> nodesWithPlanNum) {
        this.costsModel = costsModel;
        this.nodesWithPlanNum = nodesWithPlanNum;
        potentialNodes.putAll(nodesWithPlanNum);

        potentialsAlgo();
    }

    private void potentialsAlgo() {
        var potentials = calcPotentials();
        var pathCost = isOptimalSolution(potentials);
        while (pathCost.isPresent()) {
            List<Coords> pathsCycle = getPathsCycle(pathCost.get());
            optimizeSolution(pathsCycle);
            potentials = calcPotentials();
            pathCost = isOptimalSolution(potentials);
        }
    }

    private Potentials calcPotentials() {
        Map<Integer, Integer> providersPotentials = new HashMap<>();
        Map<Integer, Integer> consumersPotentials = new HashMap<>();

        BiPredicate<Integer, Integer> providerUndefined = (provider, consumer) ->
                providersPotentials.get(provider) == null && consumersPotentials.get(consumer) != null;

        BiPredicate<Integer, Integer> consumerUndefined = (provider, consumer) ->
                providersPotentials.get(provider) != null && consumersPotentials.get(consumer) == null;


        providersPotentials.put(1, 0);
        int[][] costs = costsModel.getCostsMatrix();

        while (providersPotentials.size() < costsModel.providersAmount() || consumersPotentials.size() < costsModel.consumersAmount()) {
            for (int provider = 0; provider < costsModel.providersAmount(); provider++) {
                for (int consumer = 0; consumer < costsModel.consumersAmount(); consumer++) {
                    if (consumerUndefined.test(provider, consumer))
                        consumersPotentials.put(consumer, costs[provider][consumer] - providersPotentials.get(provider));
                    else if (providerUndefined.test(provider, consumer))
                        providersPotentials.put(provider, costs[provider][consumer] - consumersPotentials.get(consumer));
                }
            }
        }

        return new Potentials(providersPotentials, consumersPotentials);
    }

    private Optional<CoordsNum> isOptimalSolution(Potentials potentials) {
        int[][] costs = costsModel.getCostsMatrix();
        CoordsNum minUnusedPathCost = null;

        boolean isOptimalSolution = true;
        for (int provider = 0; provider < costsModel.providersAmount(); provider++) {
            for (int consumer = 0; consumer < costsModel.consumersAmount(); consumer++) {

                if (nodesWithPlanNum.get(new Coords(provider, consumer)) == null) {
                    int pathCost = costs[provider][consumer] - (potentials.providers().get(provider) + potentials.consumers().get(consumer));
                    if (pathCost < 0) {
                        if (isOptimalSolution) {
                            minUnusedPathCost = new CoordsNum(new Coords(provider, consumer), pathCost);
                            isOptimalSolution = false;
                        } else if (pathCost < minUnusedPathCost.num())
                            minUnusedPathCost = new CoordsNum(new Coords(provider, consumer), pathCost);
                    }
                }
            }
        }

        return Optional.ofNullable(minUnusedPathCost);
    }

    private List<Coords> getPathsCycle(CoordsNum starterNode) {
        potentialNodes.put(starterNode.coords(), 0);

        Coords start = starterNode.coords();
        Set<Coords> foundCoords = new HashSet<>();
        Stack<Coords> planNodeCoordsStack = new Stack<>();

        Map<Coords, List<Coords>> nearestCoordsGraph = new CycleGraph(potentialNodes.keySet(), costsModel.providersAmount(),
                costsModel.consumersAmount())
                .getNearestCoords();

        foundCoords.add(start);
        planNodeCoordsStack.push(start);

        return pathCycleRecursive(nearestCoordsGraph, foundCoords, planNodeCoordsStack);
    }

    private List<Coords> pathCycleRecursive(Map<Coords, List<Coords>> nearestCoordsGraph, Set<Coords> foundCoords, Stack<Coords> planNodeCoordsStack) {
        Coords peekCoords = planNodeCoordsStack.peek();
        Coords start = planNodeCoordsStack.firstElement();

        Coords coordsFromStart = null;
        if (planNodeCoordsStack.size() > 1)
            coordsFromStart = planNodeCoordsStack.get(1);

        boolean foundNext = false;
        boolean isFoundFinal = false;

        for (Coords tmpCoords : nearestCoordsGraph.get(peekCoords)) {
            if (!foundCoords.contains(tmpCoords)) {
                foundCoords.add(tmpCoords);
                planNodeCoordsStack.push(tmpCoords);
                foundNext = true;
                break;
            } else if (tmpCoords.equals(start) && !peekCoords.equals(coordsFromStart)) {
                isFoundFinal = true;
                break;
            }
        }

        if (!foundNext && !isFoundFinal) {
            planNodeCoordsStack.pop();
            return pathCycleRecursive(nearestCoordsGraph, foundCoords, planNodeCoordsStack);
        } else {
            return planNodeCoordsStack.stream()
                    .toList();
        }
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

        if (AlgoUtils.isDegenerate(costsModel.providersAmount(), costsModel.consumersAmount(), potentialNodes.size())) {
            potentialNodes = potentialNodes.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() != 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }

}