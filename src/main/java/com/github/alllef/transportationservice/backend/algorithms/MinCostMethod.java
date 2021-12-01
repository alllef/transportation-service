package com.github.alllef.transportationservice.backend.algorithms;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MinCostMethod {
    private CostsMatrix costsMatrix;
    private List<CostNode> tmpCostsMatrix;
    private Map<Integer, CostNode> nodesWithPlanNum;
    private List<Integer> tmpStocks;
    private List<Integer> tmpNeeds;

    private List<Integer> blockedStocks;
    private List<Integer> blockedNeeds;

    public void checkIsOpenedTask() {
        if (!costsMatrix.isClosed())
            return;
    }

    public void minCostIter() {
        CostNode leastNode = tmpCostsMatrix.stream()
                .min(Comparator.comparingInt(CostNode::getCosts)).orElseThrow();

        int stockNum = leastNode.getStocksNumber();
        int needsNum = leastNode.getNeedsNumber();
        int cost = Math.min(tmpStocks.get(stockNum), tmpNeeds.get(needsNum));

        nodesWithPlanNum.put(cost, leastNode);

        tmpStocks.set(stockNum, tmpStocks.get(stockNum) - cost);
        tmpNeeds.set(needsNum, tmpStocks.get(needsNum) - cost);

        if (tmpStocks.get(stockNum) == 0 && tmpNeeds.get(needsNum) == 0)
            blockNeeds(needsNum);
        else if (tmpStocks.get(stockNum) == 0)
            blockStocks(stockNum);
        else
            blockNeeds(needsNum);
    }

    private void blockNeeds(int needsNum) {
        tmpCostsMatrix = tmpCostsMatrix.stream()
                .filter(node -> node.getNeedsNumber() != needsNum)
                .collect(Collectors.toList());

        blockedNeeds.add(needsNum);
    }

    private void blockStocks(int stocksNum) {
        tmpCostsMatrix = tmpCostsMatrix.stream()
                .filter(node -> node.getStocksNumber() != stocksNum)
                .collect(Collectors.toList());

        blockedStocks.add(stocksNum);
    }

    public int calcTransportSum() {
        int result = 0;
        for (Integer key : nodesWithPlanNum.keySet())
            result += key * nodesWithPlanNum.get(key).getCosts();

        return result;
    }
}