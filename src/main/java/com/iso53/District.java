package com.iso53;

public class District {

    private String name;
    private double lat;
    private double lon;

    public District(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public double distance(District district) {
        return Math.sqrt(Math.pow(getLat() - district.getLat(), 2) + Math.pow(getLon() - district.getLon(), 2));
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f) - %s", getLat(), getLon(), getName());
    }
}
