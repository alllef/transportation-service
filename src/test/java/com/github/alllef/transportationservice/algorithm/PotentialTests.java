package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.MinCostMethod;
import com.github.alllef.transportationservice.backend.algorithms.Potentials;
import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PotentialTests {

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
        Potentials potentials = new Potentials(costMethod);

        assertEquals(110, AlgoUtils.calcTransportSum(potentials.getPotentialNodes(), potentials.getMinCostMethod().getTmpCostsMatrix()));
    }
}
