package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.MinCostMethod;
import com.github.alllef.transportationservice.backend.algorithms.Potentials;
import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PotentialTests {

    int getTransportSum(CostsModel costsModel) {
        MinCostMethod costMethod = new MinCostMethod(costsModel);
        Potentials potentials = new Potentials(costMethod);

        return AlgoUtils.calcTransportSum(potentials.getPotentialNodes(), potentials.getMinCostMethod().getTmpCostsMatrix());
    }

    @TestFactory
    Collection<DynamicTest> transportTaskPotentialsTests() {
        return Arrays.asList(
                dynamicTest("Closed transport task", () -> assertEquals(110, getTransportSum(TestData.getClosedTransportTask()))),
                dynamicTest("Fictional provider transport task", () -> assertEquals(180, getTransportSum(TestData.getTransportTaskWithFictionalProvider()))),
                dynamicTest("Fictional consumer transport task", () -> assertEquals(195, getTransportSum(TestData.getTransportTaskWithFictionalConsumer())))
        );
    }
}
