package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CycleGraph {
    private Set<Coords> potentialCoords;
    private int providersNum;
    private int consumersNum;

    @Getter
    Map<Coords, List<Coords>> nearestCoords = new HashMap<>();

    public CycleGraph(Set<Coords> potentialCoords, int providersNum, int consumersNum) {
        this.potentialCoords = potentialCoords;
        this.providersNum = providersNum;
        this.consumersNum = consumersNum;
        buildGraph();
    }

    public void buildGraph() {
        for (Coords tmpCoords : potentialCoords) {
            List<Coords> nearestCoordsList = Stream.of(buildLeft(tmpCoords), buildRight(tmpCoords), buildUp(tmpCoords), buildDown(tmpCoords))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            nearestCoords.put(tmpCoords, nearestCoordsList);
        }
    }

    private Optional<Coords> buildLeft(Coords coordsToBuild) {
        for (int i = coordsToBuild.consumer(); i >= 0; i--) {
            Coords tmpCoords = new Coords(coordsToBuild.provider(), i);
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

    private Optional<Coords> buildRight(Coords coordsToBuild) {
        for (int i = coordsToBuild.consumer(); i < consumersNum; i++) {
            Coords tmpCoords = new Coords(coordsToBuild.provider(), i);
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

    private Optional<Coords> buildDown(Coords coordsToBuild) {
        for (int i = coordsToBuild.provider(); i >= 0; i--) {
            Coords tmpCoords = new Coords(i, coordsToBuild.consumer());
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }

        return Optional.empty();
    }

    private Optional<Coords> buildUp(Coords coordsToBuild) {
        for (int i = coordsToBuild.provider(); i < providersNum; i++) {
            Coords tmpCoords = new Coords(i, coordsToBuild.consumer());
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

}
