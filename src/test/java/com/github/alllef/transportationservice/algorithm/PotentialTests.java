package com.github.alllef.transportationservice.algorithm;

import com.github.alllef.transportationservice.backend.algorithms.CostsModel;
import com.github.alllef.transportationservice.backend.algorithms.MinCostMethod;
import com.github.alllef.transportationservice.backend.algorithms.PotentialsMethod;
import com.github.alllef.transportationservice.backend.algorithms.TransportAlgo;
import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PotentialTests {

    int getTransportSum(CostsModel costsModel) {
        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
        transportAlgo.startAlgo();

        return transportAlgo.getTransportationPrice();
    }

    @Test
    void infiniteExecutedTask() {
        CostsModel costsModel = TestData.getTransportTaskWithInfiniteExecution();
        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
         assertTimeoutPreemptively(Duration.ofSeconds(5), transportAlgo::startAlgo);
        assertTrue(lessThanProviderCapacity(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));
        assertTrue(lessThanConsumerNeeds(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));
    }

    @Test
    void noSuchElementTask() {
        CostsModel costsModel = TestData.getTransportTaskNoSuchElement();
        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
        assertTimeoutPreemptively(Duration.ofSeconds(5), transportAlgo::startAlgo);
        assertTrue(lessThanProviderCapacity(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));
        assertTrue(lessThanConsumerNeeds(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));
    }


    @RepeatedTest(20)
    void checkProperties() {
        CostsModel costsModel = TestData.randomGeneratedModel();
        System.out.println(costsModel);
        TransportAlgo transportAlgo = new TransportAlgo(costsModel);
        assertTimeoutPreemptively(Duration.ofSeconds(5), transportAlgo::startAlgo);
        assertTrue(lessThanProviderCapacity(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));
        assertTrue(lessThanConsumerNeeds(transportAlgo.getCostsModel(), transportAlgo.getNodesWithShipments()));

    }

    public boolean lessThanProviderCapacity(CostsModel costsModel, Map<Coords, Integer> nodesWithShipments) {
        for (int tmpProvider = 0; tmpProvider < costsModel.providersAmount(); tmpProvider++) {
            int sumCapacity = 0;
            for (int tmpConsumer = 0; tmpConsumer < costsModel.consumersAmount(); tmpConsumer++) {
                if (nodesWithShipments.containsKey(new Coords(tmpProvider, tmpConsumer)))
                    sumCapacity += nodesWithShipments.get(new Coords(tmpProvider, tmpConsumer));
            }
            if (sumCapacity > costsModel.getProviders().get(tmpProvider))
                return false;
        }
        return true;
    }

    public boolean lessThanConsumerNeeds(CostsModel costsModel, Map<Coords, Integer> nodesWithShipments) {
        for (int tmpConsumer = 0; tmpConsumer < costsModel.consumersAmount(); tmpConsumer++) {
            int sumNeeds = 0;
            for (int tmpProvider = 0; tmpProvider < costsModel.providersAmount(); tmpProvider++) {
                if (nodesWithShipments.containsKey(new Coords(tmpProvider, tmpConsumer)))
                    sumNeeds += nodesWithShipments.get(new Coords(tmpProvider, tmpConsumer));
            }
            if (sumNeeds > costsModel.getConsumers().get(tmpConsumer))
                return false;
        }
        return true;
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
