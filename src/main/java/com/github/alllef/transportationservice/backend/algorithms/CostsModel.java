package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CostsModel {
    private List<CostNode> costsMatrix = new ArrayList<>();
    private List<Integer> needs;
    private List<Integer> stocks;

    public CostsModel(int[][] costsMatrix, List<Integer> needs, List<Integer> stocks) {
        this.needs = needs;
        this.stocks = stocks;

        for (int i = 0; i < costsMatrix.length; i++) {
            for (int j = 0; j < costsMatrix.length; j++) {
                this.costsMatrix.add(new CostNode(i, j, costsMatrix[i][j]));
            }
        }
    }

    public boolean isClosed() {
        return Objects.equals(AlgoUtils.sum(needs), AlgoUtils.sum(stocks));
    }

}
