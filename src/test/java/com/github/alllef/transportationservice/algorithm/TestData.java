package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static CostsModel getClosedTransportTask() {
        List<Integer> providers = Arrays.asList(10, 20, 30);
        List<Integer> consumers = Arrays.asList(15, 20, 25);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{5, 3, 1};
        costsRow[1] = new int[]{3, 2, 4};
        costsRow[2] = new int[]{4, 1, 2};
        return new CostsModel(costsRow, providers, consumers);
    }

    public static CostsModel getTransportTaskWithFictionalProvider() {
        List<Integer> providers = Arrays.asList(30, 25, 20);
        List<Integer> consumers = Arrays.asList(20, 15, 25, 20);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{4, 5, 3, 6};
        costsRow[1] = new int[]{7, 2, 1, 5};
        costsRow[2] = new int[]{6, 1, 4, 2};
        return new CostsModel(costsRow, providers, consumers);
    }

    public static CostsModel getTransportTaskWithFictionalConsumer() {
        List<Integer> providers = Arrays.asList(20, 40, 30);
        List<Integer> consumers = Arrays.asList(30, 35, 20);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{3, 5, 4};
        costsRow[1] = new int[]{6, 3, 1};
        costsRow[2] = new int[]{3, 2, 7};
        return new CostsModel(costsRow, providers, consumers);
    }
}
