package com.api.des.model;


import java.util.List;
import java.util.Map;

public class PollutionData {

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public List<PollutionRecord> getList() {
        return list;
    }

    public void setList(List<PollutionRecord> list) {
        this.list = list;
    }

    private Coordinate coord;
    private List<PollutionRecord> list;


    public static class Coordinates {
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

        private double lon;
        private double lat;

        // Getters and setters
    }



    @Override
    public String toString() {
        return "PollutionData{" +
                "coord=" + coord +
                ", list=" + list +
                '}';
    }


}
