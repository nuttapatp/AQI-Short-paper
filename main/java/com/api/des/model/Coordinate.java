package com.api.des.model;

public class Coordinate {
    private double lon;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    private double lat;

    @Override
    public String toString() {
        return "Coordinate{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }

}
