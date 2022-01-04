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
    private int[][] costsMatrix;
    private List<Integer> providers;
    private List<Integer> consumers;

    public boolean isClosed() {
        return Objects.equals(AlgoUtils.sum(providers), AlgoUtils.sum(consumers));
    }

    public int providersAmount() {
        return providers.size();
    }

    public int consumersAmount() {
        return consumers.size();
    }
}
