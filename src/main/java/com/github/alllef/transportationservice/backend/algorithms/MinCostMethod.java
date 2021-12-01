package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
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

    private List<CostNode> tmpCostsMatrix = new ArrayList<>();
    private List<Integer> tmpStocks = new ArrayList<>();
    private List<Integer> tmpNeeds = new ArrayList<>();

    private Map<Integer, CostNode> nodesWithPlanNum = new HashMap<>();
    private List<Integer> blockedStocks = new ArrayList<>();
    private List<Integer> blockedNeeds = new ArrayList<>();

    public MinCostMethod(CostsModel costsModel) {
        this.costsModel = costsModel;
        tmpCostsMatrix.addAll(costsModel.getCostsMatrix());
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
                    tmpCostsMatrix.add(new CostNode(i, costsModel.getStocks().size() + 1, -1));
            } else {
                int additionalNeeds = AlgoUtils.sum(costsModel.getStocks()) - AlgoUtils.sum(costsModel.getNeeds());
                for (int i = 0; i < costsModel.getStocks().size(); i++)
                    tmpCostsMatrix.add(new CostNode(costsModel.getNeeds().size() + 1, i, -1));
                tmpNeeds.add(additionalNeeds);
            }
        }
    }

    public void minCostAlgo() {
        while (blockedStocks.size() != tmpStocks.size() && tmpNeeds.size() != blockedNeeds.size())
            minCostIter();
    }

    public void minCostIter() {
        CostNode leastNode = tmpCostsMatrix.stream()
                .min(Comparator.comparingInt(CostNode::getCosts))
                .orElseThrow();

        int stockNum = leastNode.getStocksNumber();
        int needsNum = leastNode.getNeedsNumber();
        int cost = Math.min(tmpStocks.get(stockNum), tmpNeeds.get(needsNum));

        nodesWithPlanNum.put(cost, leastNode);

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

    private boolean isDegenerate() {
        return (tmpStocks.size() + tmpNeeds.size() - 1) != nodesWithPlanNum.size();
    }
}