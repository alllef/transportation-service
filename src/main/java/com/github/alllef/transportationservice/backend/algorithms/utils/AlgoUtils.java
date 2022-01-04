package com.github.alllef.transportationservice.backend.algorithms.utils;

import java.util.Collection;
import java.util.Map;

public class AlgoUtils {
    public static int sum(Collection<Integer> collection) {
        return collection.stream()
                .reduce(0, Integer::sum);
    }

    public static int calcTransportSum(Map<Coords, Integer> transportationNums, int[][] priceNums) {
        int result = 0;
        for (Coords key : transportationNums.keySet())
            result += transportationNums.get(key) * priceNums[key.provider()][key.consumer()];

        return result;
    }

    public static boolean isDegenerate(int providersNum, int consumersNum, int nodesWithPlanNum) {
        return (providersNum + consumersNum - 1) != nodesWithPlanNum;
    }

}
