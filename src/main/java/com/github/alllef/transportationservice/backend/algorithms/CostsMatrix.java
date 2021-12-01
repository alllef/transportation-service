package com.github.alllef.transportationservice.backend.algorithms;

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
public class CostsMatrix {
    private List<CostNode> costsMatrix;
    List<Integer> needs;
    List<Integer> stocks;

    public boolean isClosed() {
        return Objects.equals(needs.stream().reduce(0, Integer::sum), stocks.stream().reduce(0, Integer::sum));
    }
}
