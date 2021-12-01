package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.AlgoUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CostsModel {
    private List<CostNode> costsMatrix;
    private List<Integer> needs;
    private List<Integer> stocks;

    public boolean isClosed() {
        return Objects.equals(AlgoUtils.sum(needs), AlgoUtils.sum(stocks));
    }


}
