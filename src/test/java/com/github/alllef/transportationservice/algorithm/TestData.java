package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public static CostsModel getTransportTaskWithInfiniteExecution() {
        List<Integer> providers = Arrays.asList(14, 28, 26, 1);
        List<Integer> consumers = Arrays.asList(16, 1, 26, 27);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{12, 18, 18, 15};
        costsRow[1] = new int[]{13, 4, 5, 4};
        costsRow[2] = new int[]{16, 7, 9, 9};
        costsRow[3] = new int[]{9, 17, 19, 4};
        return new CostsModel(costsRow, providers, consumers);
    }

    public static CostsModel getSecondTransportTaskWithInfiniteExecution() {
        List<Integer> providers = Arrays.asList(2, 5, 15, 3);
        List<Integer> consumers = Arrays.asList(20, 3, 12);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{12, 20, 1};
        costsRow[1] = new int[]{5, 17, 6};
        costsRow[2] = new int[]{2, 8, 20};
        costsRow[3] = new int[]{10, 2, 14};
        return new CostsModel(costsRow, providers, consumers);
    }

    public static CostsModel getTransportTaskNoSuchElement() {
        List<Integer> providers = Arrays.asList(25, 13, 11);
        List<Integer> consumers = Arrays.asList(15, 12, 24);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{9, 3, 8};
        costsRow[1] = new int[]{13, 7, 18};
        costsRow[2] = new int[]{7, 1, 15};
        return new CostsModel(costsRow, providers, consumers);
    }

    public static CostsModel randomGeneratedModel() {
        Random rand = new Random();
        int providersNum = rand.nextInt(3) + 2;
        int consumersNum = rand.nextInt(4) + 2;
        int[][] costsRow = new int[providersNum][consumersNum];

        List<Integer> providers = new ArrayList<>();
        List<Integer> consumers = new ArrayList<>();
        for (int i = 0; i < providersNum; i++)
            providers.add(rand.nextInt(30) + 1);

        for (int j = 0; j < consumersNum; j++)
            consumers.add(rand.nextInt(30) + 1);
        for (int i = 0; i < costsRow.length; i++) {
            for (int j = 0; j < costsRow[i].length; j++) {
                costsRow[i][j] = rand.nextInt(20) + 1;
            }
        }

        return new CostsModel(costsRow, providers, consumers);
    }
}
