package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.MinCostMethod;
import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MinCostMethodTests {

    // Example is from http://reshmat.ru/example_transport_1.html
    @Test
    void testClosedTask() {
        List<Integer> providers = Arrays.asList(10, 20, 30);
        List<Integer> consumers = Arrays.asList(15, 20, 25);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{5, 3, 1};
        costsRow[1] = new int[]{3, 2, 4};
        costsRow[2] = new int[]{4, 1, 2};
        CostsModel costsModel = new CostsModel(costsRow, providers, consumers);

        MinCostMethod costMethod = new MinCostMethod(costsModel);
        assertEquals(115, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(),costMethod.getTmpCostsMatrix()));
    }

    // Example is from http://reshmat.ru/example_transport_2.html
    @Test
    void testOpenTaskWithFictionalProvider() {
        List<Integer> providers = Arrays.asList(30, 25, 20);
        List<Integer> consumers = Arrays.asList(20, 15, 25, 20);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{4, 5, 3, 6};
        costsRow[1] = new int[]{7, 2, 1, 5};
        costsRow[2] = new int[]{6, 1, 4, 2};
        CostsModel costsModel = new CostsModel(costsRow, providers, consumers);

        MinCostMethod costMethod = new MinCostMethod(costsModel);
        assertEquals(providers.size() + 1, costMethod.getTmpProviders().size());
        assertEquals(190, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(),costMethod.getTmpCostsMatrix()));
    }

    // Example is from http://reshmat.ru/example_transport_3.html
    @Test
    void testOpenTaskWithFictionalConsumer() {
        List<Integer> providers = Arrays.asList(20, 40, 30);
        List<Integer> consumers = Arrays.asList(30, 35, 20);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{3, 5, 4};
        costsRow[1] = new int[]{6, 3, 1};
        costsRow[2] = new int[]{3, 2, 7};
        CostsModel costsModel = new CostsModel(costsRow, providers, consumers);

        MinCostMethod costMethod = new MinCostMethod(costsModel);
        assertEquals(215, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(),costMethod.getTmpCostsMatrix()));
        assertEquals(consumers.size() + 1, costMethod.getTmpConsumers().size());
    }
}
