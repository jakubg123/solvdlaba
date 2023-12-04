package com.solvd.agency.enums;

public enum Season {
    SPRING("Mild"),
    SUMMER("Hot"),
    AUTUMN("Cool"),
    WINTER("Cold");

    private String weatherDescription;

    Season(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public static Season getSeasonFromMonth(java.time.Month month) {
        switch (month) {
            case DECEMBER:
            case JANUARY:
            case FEBRUARY:
                return Season.WINTER;
            case MARCH:
            case APRIL:
            case MAY:
                return Season.SPRING;
            case JUNE:
            case JULY:
            case AUGUST:
                return Season.SUMMER;
            case SEPTEMBER:
            case OCTOBER:
            case NOVEMBER:
                return Season.AUTUMN;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }


}
