package com.solvd.agency.enums;

public enum TransportType {
    BUS("Bus",60),
    MINIBUS("Mini-bus", 65),
    CAR("Car", 4) {
        @Override
        public String getTravelAdvice() {
            return "Comfortable transport for a family";
        }
    };

    private final String name;
    private final int averageSpeed;

    TransportType(String name, int averageSpeed) {
        this.name = name;
        this.averageSpeed = averageSpeed;
    }

    public String getName() {
        return name;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public String getTravelAdvice() {
        return "Good for short - medium travels";
    }

    @Override
    public String toString() {
        return this.name + "Speed: " + this.averageSpeed + " km/h";
    }
}