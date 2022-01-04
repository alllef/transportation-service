package com.github.alllef.transportationservice.backend.algorithms;

import static com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils.*;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
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
    private Map<Coords, Integer> additionalBlockedValueMatrix = new HashMap<>();

    public MinCostMethod(CostsModel costsModel) {
        this.costsModel = costsModel;
        tmpCostsMatrix.putAll(costsModel.getCostsMatrix());
        tmpProviders.addAll(costsModel.getProviders());
        tmpConsumers.addAll(costsModel.getConsumers());
        startAlgo();
    }

    private void startAlgo() {
        if (!costsModel.isClosed())
            convertOpenTaskToClosed();

        additionalBlockedValueMatrix.putAll(tmpCostsMatrix);

        minCostAlgo();
        if (isDegenerate(tmpProviders.size(), tmpConsumers.size(), nodesWithPlanNum.size()))
            throw new RuntimeException("Is degenerate. It is not possible");
    }

    private void convertOpenTaskToClosed() {
        int providerSum = sum(costsModel.getProviders());
        int consumerSum = sum(costsModel.getConsumers());

        if (providerSum > consumerSum) {
            int additionalConsumer = providerSum - consumerSum;
            tmpConsumers.add(additionalConsumer);
            for (int i = 0; i < costsModel.getProviders().size(); i++)
                tmpCostsMatrix.put(new Coords(i, costsModel.getConsumers().size()), Integer.MAX_VALUE);
        } else {
            int additionalProvider = consumerSum - providerSum;
            tmpProviders.add(additionalProvider);
            for (int i = 0; i < costsModel.getConsumers().size(); i++)
                tmpCostsMatrix.put(new Coords(costsModel.getProviders().size(), i),
                        Integer.MAX_VALUE);
        }
    }

    private void minCostAlgo() {
        while (blockedProvidersKeys.size() != tmpProviders.size()
                || blockedConsumersKeys.size() != tmpConsumers.size())
            minCostIter();
    }

    private void minCostIter() {
        Map.Entry<Coords, Integer> leastNode = additionalBlockedValueMatrix.entrySet()
                .stream()
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .orElseThrow();

        if (leastNode.getValue() == Integer.MAX_VALUE) {
            leastNode.setValue(0);
            tmpCostsMatrix.put(leastNode.getKey(), leastNode.getValue());
        }

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
        additionalBlockedValueMatrix = additionalBlockedValueMatrix.entrySet()
                .stream()
                .filter(node -> switch (type) {
                    case PROVIDER -> node.getKey().provider() != entityKey;
                    case CONSUMER -> node.getKey().consumer() != entityKey;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        switch (type) {
            case PROVIDER -> blockedProvidersKeys.add(entityKey);
            case CONSUMER -> blockedConsumersKeys.add(entityKey);
        }
    }
}