package com.github.alllef.transportationservice.backend.algorithms.utils;

import java.util.Collection;

public class AlgoUtils {
    public static int sum(Collection<Integer> collection) {
        return collection.stream()
                .reduce(0, Integer::sum);
    }
}
