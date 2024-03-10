package com.iso53;

public class Headquarter {

    public final double lat;
    public final double lon;
    public final int index;

    public Headquarter(int index, double lat, double lon) {
        this.index = index;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f) | Headquarter | Index: %d", this.lat, this.lon, this.index);
    }
}
