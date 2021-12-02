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
    private List<Integer> needs;
    private List<Integer> stocks;

    public CostsModel(int[][] costsMatrix, List<Integer> needs, List<Integer> stocks) {
        this.needs = needs;
        this.stocks = stocks;

        for (int i = 0; i < costsMatrix.length; i++) {
            for (int j = 0; j < costsMatrix.length; j++) {
                this.costsMatrix.put(new Coords(i,j), costsMatrix[i][j]);
            }
        }
    }

    public boolean isClosed() {
        return Objects.equals(AlgoUtils.sum(needs), AlgoUtils.sum(stocks));
    }

}
