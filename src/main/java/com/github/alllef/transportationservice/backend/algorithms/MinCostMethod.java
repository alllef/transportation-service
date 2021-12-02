package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class MinCostMethod {
    private CostsModel costsModel;

    private Map<Coords, Integer> tmpCostsMatrix = new HashMap<>();
    private List<Integer> tmpStocks = new ArrayList<>();
    private List<Integer> tmpNeeds = new ArrayList<>();

    private Map<Coords, Integer> nodesWithPlanNum = new HashMap<>();
    private List<Integer> blockedStocks = new ArrayList<>();
    private List<Integer> blockedNeeds = new ArrayList<>();

    public MinCostMethod(CostsModel costsModel) {
        this.costsModel = costsModel;
        tmpCostsMatrix.putAll(costsModel.getCostsMatrix());
        tmpStocks.addAll(costsModel.getStocks());
        tmpNeeds.addAll(costsModel.getNeeds());
    }

    public int startAlgo() {
        checkIsOpenedTask();
        minCostAlgo();
        if (isDegenerate()) throw new RuntimeException("Is degenerate. It is not possible");
        else
            return calcTransportSum();
    }

    public void checkIsOpenedTask() {
        if (!costsModel.isClosed()) {
            if (AlgoUtils.sum(costsModel.getNeeds()) > AlgoUtils.sum(costsModel.getStocks())) {
                int additionalStocks = AlgoUtils.sum(costsModel.getNeeds()) - AlgoUtils.sum(costsModel.getStocks());
                tmpStocks.add(additionalStocks);
                for (int i = 0; i < costsModel.getNeeds().size(); i++)
                    tmpCostsMatrix.put(new Coords(i, costsModel.getStocks().size()), -1);
            } else {
                int additionalNeeds = AlgoUtils.sum(costsModel.getStocks()) - AlgoUtils.sum(costsModel.getNeeds());
                for (int i = 0; i < costsModel.getStocks().size(); i++)
                    tmpCostsMatrix.put(new Coords(costsModel.getNeeds().size(), i), -1);
                tmpNeeds.add(additionalNeeds);
            }
        }
    }

    public void minCostAlgo() {
        while (blockedStocks.size() != tmpStocks.size() && tmpNeeds.size() != blockedNeeds.size())
            minCostIter();
    }

    public void minCostIter() {
        Map.Entry<Coords, Integer> leastNode = tmpCostsMatrix.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();

        int stockNum = leastNode.getKey().stocks();
        int needsNum = leastNode.getKey().needs();
        int cost = Math.min(tmpStocks.get(stockNum), tmpNeeds.get(needsNum));

        nodesWithPlanNum.put(leastNode.getKey(), leastNode.getValue());

        tmpStocks.set(stockNum, tmpStocks.get(stockNum) - cost);
        tmpNeeds.set(needsNum, tmpStocks.get(needsNum) - cost);

        if (blockedStocks.size() == tmpStocks.size() - 1 && blockedNeeds.size() == tmpNeeds.size() - 1) {
            blockNeeds(needsNum);
            blockStocks(stockNum);
        } else if (tmpStocks.get(stockNum) == 0 && tmpNeeds.get(needsNum) == 0)
            blockNeeds(needsNum);
        else if (tmpStocks.get(stockNum) == 0)
            blockStocks(stockNum);
        else
            blockNeeds(needsNum);
    }

    private void blockNeeds(int needsNum) {
        tmpCostsMatrix = tmpCostsMatrix.entrySet()
                .stream()
                .filter(node -> node.getKey().needs() != needsNum)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        blockedNeeds.add(needsNum);
    }

    private void blockStocks(int stocksNum) {
        tmpCostsMatrix = tmpCostsMatrix.entrySet()
                .stream()
                .filter(node -> node.getKey().stocks() != stocksNum)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        blockedStocks.add(stocksNum);
    }

    public int calcTransportSum() {
        int result = 0;
        for (Coords key : nodesWithPlanNum.keySet())
            result += nodesWithPlanNum.get(key) * costsModel.getCostsMatrix().get(key);

        return result;
    }

    private boolean isDegenerate() {
        return (tmpStocks.size() + tmpNeeds.size() - 1) != nodesWithPlanNum.size();
    }
}