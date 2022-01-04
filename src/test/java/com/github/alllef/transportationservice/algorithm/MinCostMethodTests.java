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
        MinCostMethod costMethod = new MinCostMethod(TestData.getClosedTransportTask());

        costMethod.startAlgo();
        assertEquals(115, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(), costMethod.getCostsModel().getCostsMatrix()));
    }

    // Example is from http://reshmat.ru/example_transport_2.html
    @Test
    void testOpenTaskWithFictionalProvider() {
        MinCostMethod costMethod = new MinCostMethod(TestData.getTransportTaskWithFictionalProvider());
        costMethod.startAlgo();

        assertEquals(costMethod.getCostsModel().getProviders().size() , costMethod.getCostsModel().providersAmount());
        assertEquals(190, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(), costMethod.getCostsModel().getCostsMatrix()));
    }

    // Example is from http://reshmat.ru/example_transport_3.html
    @Test
    void testOpenTaskWithFictionalConsumer() {
        MinCostMethod costMethod = new MinCostMethod(TestData.getTransportTaskWithFictionalConsumer());
        costMethod.startAlgo();

        assertEquals(costMethod.getCostsModel().getConsumers().size(), costMethod.getCostsModel().consumersAmount());
        assertEquals(215, AlgoUtils.calcTransportSum(costMethod.getNodesWithPlanNum(), costMethod.getCostsModel().getCostsMatrix()));

    }
}
