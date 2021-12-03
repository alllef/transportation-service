package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.enums.Coords;
import com.github.alllef.transportationservice.backend.algorithms.utils.enums.EntityType;
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
        tmpProviders.addAll(costsModel.getProviders());
        tmpConsumers.addAll(costsModel.getConsumers());
    }

    public int startAlgo() {
        checkIsOpenedTask();
        minCostAlgo();
        if (isDegenerate()) throw new RuntimeException("Is degenerate. It is not possible");
        else
            return calcTransportSum();
    }

    private void checkIsOpenedTask() {

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

    private void minCostAlgo() {
        while (blockedProvidersKeys.size() != tmpProviders.size() || blockedConsumersKeys.size() != tmpConsumers.size())
            minCostIter();
    }

    private void minCostIter() {
        Map.Entry<Coords, Integer> leastNode = tmpCostsMatrix.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();

        int providerKey = leastNode.getKey().provider();
        int consumerKey = leastNode.getKey().consumer();

        int cost = Math.min(tmpProviders.get(providerKey), tmpConsumers.get(consumerKey));

        nodesWithPlanNum.put(leastNode.getKey(), cost);

        tmpProviders.set(providerKey, tmpProviders.get(providerKey) - cost);
        tmpConsumers.set(consumerKey, tmpConsumers.get(consumerKey) - cost);

        blockEntities(providerKey, consumerKey);
    }

    private void blockEntities(int providerKey, int consumerKey) {
        if (blockedProvidersKeys.size() == (tmpProviders.size() - 1) && blockedConsumersKeys.size() == (tmpConsumers.size() - 1)) {
            blockEntity(providerKey, EntityType.PROVIDER);
            blockEntity(consumerKey, EntityType.CONSUMER);
        } else if (tmpProviders.get(providerKey) == 0 && tmpConsumers.get(consumerKey) == 0)
            blockEntity(providerKey, EntityType.PROVIDER);
        else if (tmpConsumers.get(consumerKey) == 0)
            blockEntity(consumerKey, EntityType.CONSUMER);
        else
            blockEntity(providerKey, EntityType.PROVIDER);
    }

    private void blockEntity(int entityKey, EntityType type) {
        tmpCostsMatrix = tmpCostsMatrix.entrySet()
                .stream()
                .filter(node -> {
                    if (type == EntityType.PROVIDER) return node.getKey().provider() != entityKey;
                    else return node.getKey().consumer() != entityKey;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (type == EntityType.CONSUMER) blockedConsumersKeys.add(entityKey);
        else blockedProvidersKeys.add(entityKey);
    }

    private int calcTransportSum() {
        int result = 0;
        for (Coords key : nodesWithPlanNum.keySet())
            result += nodesWithPlanNum.get(key) * costsModel.getCostsMatrix().get(key);

        return result;
    }

    private boolean isDegenerate() {
        return (tmpProviders.size() + tmpConsumers.size() - 1) != nodesWithPlanNum.size();
    }
}