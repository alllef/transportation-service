package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
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
    private List<Integer> tmpProviders = new ArrayList<>();
    private List<Integer> tmpConsumers = new ArrayList<>();

    private Map<Coords, Integer> nodesWithPlanNum = new HashMap<>();
    private List<Integer> blockedProvidersKeys = new ArrayList<>();
    private List<Integer> blockedConsumersKeys = new ArrayList<>();

    public MinCostMethod(CostsModel costsModel) {
        this.costsModel = costsModel;
        tmpCostsMatrix.putAll(costsModel.getCostsMatrix());
        tmpProviders.addAll(costsModel.getConsumers());
        tmpConsumers.addAll(costsModel.getProviders());
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
            if (AlgoUtils.sum(costsModel.getProviders()) > AlgoUtils.sum(costsModel.getConsumers())) {
                int additionalConsumer = AlgoUtils.sum(costsModel.getProviders()) - AlgoUtils.sum(costsModel.getConsumers());
                tmpProviders.add(additionalConsumer);
                for (int i = 0; i < costsModel.getProviders().size(); i++)
                    tmpCostsMatrix.put(new Coords(i, costsModel.getConsumers().size()), -1);
            } else {
                int additionalProvider = AlgoUtils.sum(costsModel.getConsumers()) - AlgoUtils.sum(costsModel.getProviders());
                for (int i = 0; i < costsModel.getConsumers().size(); i++)
                    tmpCostsMatrix.put(new Coords(costsModel.getProviders().size(), i), -1);
                tmpConsumers.add(additionalProvider);
            }
        }
    }

    public void minCostAlgo() {
        while (blockedProvidersKeys.size() != tmpProviders.size() && tmpConsumers.size() != blockedConsumersKeys.size())
            minCostIter();
    }

    public void minCostIter() {
        Map.Entry<Coords, Integer> leastNode = tmpCostsMatrix.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();

        int consumerKey = leastNode.getKey().consumer();
        int providerKey = leastNode.getKey().provider();
        int cost = Math.min(tmpProviders.get(consumerKey), tmpConsumers.get(providerKey));

        nodesWithPlanNum.put(leastNode.getKey(), leastNode.getValue());

        tmpProviders.set(consumerKey, tmpProviders.get(consumerKey) - cost);
        tmpConsumers.set(providerKey, tmpProviders.get(providerKey) - cost);

        if (blockedProvidersKeys.size() == tmpProviders.size() - 1 && blockedConsumersKeys.size() == tmpConsumers.size() - 1) {
            blockProvider(providerKey);
            blockConsumer(consumerKey);
        } else if (tmpProviders.get(consumerKey) == 0 && tmpConsumers.get(providerKey) == 0)
            blockProvider(providerKey);
        else if (tmpProviders.get(consumerKey) == 0)
            blockConsumer(consumerKey);
        else
            blockProvider(providerKey);
    }

    private void blockProvider(int providerKey) {
        tmpCostsMatrix = tmpCostsMatrix.entrySet()
                .stream()
                .filter(node -> node.getKey().provider() != providerKey)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        blockedConsumersKeys.add(providerKey);
    }

    private void blockConsumer(int consumerKey) {
        tmpCostsMatrix = tmpCostsMatrix.entrySet()
                .stream()
                .filter(node -> node.getKey().consumer() != consumerKey)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        blockedProvidersKeys.add(consumerKey);
    }

    public int calcTransportSum() {
        int result = 0;
        for (Coords key : nodesWithPlanNum.keySet())
            result += nodesWithPlanNum.get(key) * costsModel.getCostsMatrix().get(key);

        return result;
    }

    private boolean isDegenerate() {
        return (tmpProviders.size() + tmpConsumers.size() - 1) != nodesWithPlanNum.size();
    }
}