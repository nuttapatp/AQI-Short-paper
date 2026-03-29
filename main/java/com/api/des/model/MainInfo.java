package com.api.des.model;

public class MainInfo {
    private int aqi;

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }
// Getters and Setters


    @Override
    public String toString() {
        return "Main{" +
                "aqi=" + aqi +
                '}';
    }
}
