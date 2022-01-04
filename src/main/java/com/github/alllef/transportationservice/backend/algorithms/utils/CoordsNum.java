package com.github.alllef.transportationservice.backend.algorithms.utils;

public record CoordsNum(Coords coords, int num) {

    public int provider() {
        return coords.provider();
    }

    public int consumer() {
        return coords.consumer();
    }
}
