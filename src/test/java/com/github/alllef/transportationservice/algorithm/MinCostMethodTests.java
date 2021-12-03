package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.MinCostMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MinCostMethodTests {
    CostsModel costsModel;

    // Example is from http://reshmat.ru/example_transport_1.html
    @BeforeAll
    void configureTest() {
        List<Integer> providers = Arrays.asList(10, 20, 30);
        List<Integer> consumers = Arrays.asList(15, 20, 25);
        int[][] costsRow = new int[providers.size()][consumers.size()];
        costsRow[0] = new int[]{5, 3, 1};
        costsRow[1] = new int[]{3, 2, 4};
        costsRow[2] = new int[]{4, 1, 2};
        costsModel = new CostsModel(costsRow, providers, consumers);
    }

    @Test
    void testCalculation() {
        MinCostMethod costMethod = new MinCostMethod(costsModel);
        assertEquals(115, costMethod.startAlgo());
    }
}
