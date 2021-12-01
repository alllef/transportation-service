package com.github.alllef.transportationservice.backend.algorithms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CostNode {
    private int needsNumber;
    private int stocksNumber;
    private int costs;
}
