package com.github.alllef.transportationservice.backend.algorithms;

import com.github.alllef.transportationservice.backend.algorithms.utils.Coords;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CycleGraph {
    private Set<Coords> potentialCoords = new HashSet<>();
    private int providersNum;
    private int consumersNum;

    @Getter
    private Map<Coords, List<Coords>> nearestCoords = new HashMap<>();

    public CycleGraph(Set<Coords> potentialCoords, int providersNum, int consumersNum) {
        for (Coords coords : potentialCoords)
            this.potentialCoords.add(new Coords(coords.provider(), coords.consumer()));

        this.providersNum = providersNum;
        this.consumersNum = consumersNum;
        buildGraph();
    }

    private void buildGraph() {
        removeRowsColumnsWithSingleElem();
        for (Coords tmpCoords : potentialCoords) {
            List<Coords> nearestCoordsList = Stream.of(buildLeft(tmpCoords), buildRight(tmpCoords), buildUp(tmpCoords), buildDown(tmpCoords))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            nearestCoords.put(tmpCoords, nearestCoordsList);
        }
    }

    private void removeRowsColumnsWithSingleElem() {
        Map<Integer, Integer> rowsWithSingleElem = new HashMap<>();
        Map<Integer, Integer> columnsWithSingleElem = new HashMap<>();

        boolean areRemovedRowsColumns = false;

        for (Coords coords : potentialCoords) {
            rowsWithSingleElem.putIfAbsent(coords.provider(), 0);
            columnsWithSingleElem.putIfAbsent(coords.consumer(), 0);
            rowsWithSingleElem.put(coords.provider(), rowsWithSingleElem.get(coords.provider()) + 1);
            columnsWithSingleElem.put(coords.consumer(), columnsWithSingleElem.get(coords.consumer()) + 1);
        }

        for (Iterator<Coords> iterator = potentialCoords.iterator(); iterator.hasNext(); ) {
            Coords coords = iterator.next();
            if (rowsWithSingleElem.get(coords.provider()) == 1 || columnsWithSingleElem.get(coords.consumer()) == 1) {
                iterator.remove();
                areRemovedRowsColumns = true;
            }
        }

        if (areRemovedRowsColumns)
            removeRowsColumnsWithSingleElem();

    }

    private Optional<Coords> buildLeft(Coords coordsToBuild) {
        for (int i = coordsToBuild.consumer() - 1; i >= 0; i--) {
            Coords tmpCoords = new Coords(coordsToBuild.provider(), i);
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

    private Optional<Coords> buildRight(Coords coordsToBuild) {
        for (int i = coordsToBuild.consumer() + 1; i < consumersNum; i++) {
            Coords tmpCoords = new Coords(coordsToBuild.provider(), i);
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

    private Optional<Coords> buildDown(Coords coordsToBuild) {
        for (int i = coordsToBuild.provider() - 1; i >= 0; i--) {
            Coords tmpCoords = new Coords(i, coordsToBuild.consumer());
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }

        return Optional.empty();
    }

    private Optional<Coords> buildUp(Coords coordsToBuild) {
        for (int i = coordsToBuild.provider() + 1; i < providersNum; i++) {
            Coords tmpCoords = new Coords(i, coordsToBuild.consumer());
            if (potentialCoords.contains(tmpCoords))
                return Optional.of(tmpCoords);
        }
        return Optional.empty();
    }

}
