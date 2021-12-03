package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CostsModel {
    private Map<Coords,Integer> costsMatrix = new HashMap<>();
    private List<Integer> providers;
    private List<Integer> consumers;

    public CostsModel(int[][] costsMatrix, List<Integer> providers, List<Integer> consumers) {
        this.providers = providers;
        this.consumers = consumers;

        for (int tmpProvider = 0; tmpProvider < providers.size(); tmpProvider++) {
            for (int tmpConsumer  = 0; tmpConsumer < consumers.size(); tmpConsumer++) {
                this.costsMatrix.put(new Coords(tmpProvider,tmpConsumer), costsMatrix[tmpProvider][tmpConsumer]);
            }
        }
    }

    public boolean isClosed() {
        return Objects.equals(AlgoUtils.sum(providers), AlgoUtils.sum(consumers));
    }

}
