package com.github.alllef.transportationservice.backend.algorithms;

import static com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils.*;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import com.github.alllef.transportationservice.backend.algorithms.utils.CoordsNum;
import com.github.alllef.transportationservice.backend.algorithms.utils.enums.EntityType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class MinCostMethod {
    private CostsModel costsModel;

    private int[][] tmpCostsMatrix;
    private List<Integer> tmpProviders = new ArrayList<>();
    private List<Integer> tmpConsumers = new ArrayList<>();

    @Getter
    private Map<Coords, Integer> nodesWithPlanNum = new HashMap<>();

    private List<Integer> blockedProvidersKeys = new ArrayList<>();
    private List<Integer> blockedConsumersKeys = new ArrayList<>();
    private boolean[][] additionalBlockedValueMatrix;

    public MinCostMethod(CostsModel costsModel) {
        this.costsModel = costsModel;

        tmpProviders.addAll(costsModel.getProviders());
        tmpConsumers.addAll(costsModel.getConsumers());

        tmpCostsMatrix = new int[tmpProviders.size() + 1][tmpConsumers.size() + 1];
        for (int i = 0; i < tmpProviders.size(); i++) {
            for (int j = 0; j < tmpConsumers.size(); j++)
                tmpCostsMatrix[i][j] = costsModel.getCostsMatrix()[i][j];
        }

        additionalBlockedValueMatrix = new boolean[tmpProviders.size() + 1][tmpConsumers.size() + 1];
    }

    public CostsModel getCostsModel() {
        return new CostsModel(tmpCostsMatrix, tmpProviders, tmpConsumers);
    }

    public void startAlgo() {
        if (!costsModel.isClosed())
            convertOpenTaskToClosed();

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
            for (int i = 0; i < costsModel.providersAmount(); i++)
                tmpCostsMatrix[i][costsModel.consumersAmount()] = Integer.MAX_VALUE;
        } else {
            int additionalProvider = consumerSum - providerSum;
            tmpProviders.add(additionalProvider);
            for (int i = 0; i < costsModel.consumersAmount(); i++)
                tmpCostsMatrix[costsModel.providersAmount()][i] = Integer.MAX_VALUE;
        }
    }

    private void minCostAlgo() {
        while (blockedProvidersKeys.size() != tmpProviders.size()
                || blockedConsumersKeys.size() != tmpConsumers.size())
            minCostIter();
    }

    private void minCostIter() {
        List<CoordsNum> unblockedValues = new ArrayList<>();

        for (int tmpProvider = 0; tmpProvider < tmpProviders.size(); tmpProvider++) {
            for (int tmpConsumer = 0; tmpConsumer < tmpConsumers.size(); tmpConsumer++) {
                if (!additionalBlockedValueMatrix[tmpProvider][tmpConsumer])
                    unblockedValues.add(new CoordsNum(new Coords(tmpProvider, tmpConsumer), tmpCostsMatrix[tmpProvider][tmpConsumer]));
            }
        }

        CoordsNum leastNode = unblockedValues
                .stream()
                .min(Comparator.comparingInt(CoordsNum::num))
                .orElseThrow();

        int providerKey = leastNode.provider();
        int consumerKey = leastNode.consumer();

        if (leastNode.num() == Integer.MAX_VALUE)
            tmpCostsMatrix[providerKey][consumerKey] = 0;

        int cost = Math.min(tmpProviders.get(providerKey), tmpConsumers.get(consumerKey));

        nodesWithPlanNum.put(leastNode.coords(), cost);

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
        switch (type) {
            case PROVIDER -> {
                for (int i = 0; i < additionalBlockedValueMatrix[entityKey].length; i++)
                    additionalBlockedValueMatrix[entityKey][i] = true;

                blockedProvidersKeys.add(entityKey);
            }
            case CONSUMER -> {
                for (int j = 0; j < additionalBlockedValueMatrix.length; j++)
                    additionalBlockedValueMatrix[j][entityKey] = true;

                blockedConsumersKeys.add(entityKey);
            }
        }
    }
}