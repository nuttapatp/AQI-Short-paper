package com.api.des.model;

public class PollutionRecord {
    private Main main;
    private Components components;
    private long dt; // UNIX timestamp

    // Getters and Setters for main, components, and dt
    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    @Override
    public String toString() {
        return "PollutionRecord{" +
                "main=" + main +
                ", components=" + components +
                ", dt=" + dt +
                '}';
    }

    public static class Main {
        private int aqi;

        // Getters and Setters
        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        @Override
        public String toString() {
            return "Main{" +
                    "aqi=" + aqi +
                    '}';
        }
    }

    public static class Components {
        private double co;
        private double no;
        private double no2;
        private double o3;
        private double so2;
        private double pm2_5;
        private double pm10;
        private double nh3;

        public double getCo() {
            return co;
        }

        public void setCo(double co) {
            this.co = co;
        }

        public double getNo() {
            return no;
        }

        public void setNo(double no) {
            this.no = no;
        }

        public double getNo2() {
            return no2;
        }

        public void setNo2(double no2) {
            this.no2 = no2;
        }

        public double getO3() {
            return o3;
        }

        public void setO3(double o3) {
            this.o3 = o3;
        }

        public double getSo2() {
            return so2;
        }

        public void setSo2(double so2) {
            this.so2 = so2;
        }

        public double getPm2_5() {
            return pm2_5;
        }

        public void setPm2_5(double pm2_5) {
            this.pm2_5 = pm2_5;
        }

        public double getPm10() {
            return pm10;
        }

        public void setPm10(double pm10) {
            this.pm10 = pm10;
        }

        public double getNh3() {
            return nh3;
        }

        public void setNh3(double nh3) {
            this.nh3 = nh3;
        }

        @Override
        public String toString() {
            return "Components{" +
                    "co=" + co +
                    ", no=" + no +
                    ", no2=" + no2 +
                    ", o3=" + o3 +
                    ", so2=" + so2 +
                    ", pm2_5=" + pm2_5 +
                    ", pm10=" + pm10 +
                    ", nh3=" + nh3 +
                    '}';
        }
    }
}
