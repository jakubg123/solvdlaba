package com.solvd.agency.enums;

public enum Month {
    JANUARY(Season.WINTER),
    FEBRUARY(Season.WINTER),
    MARCH(Season.SPRING),
    APRIL(Season.SPRING),
    MAY(Season.SPRING),
    JUNE(Season.SUMMER),
    JULY(Season.SUMMER),
    AUGUST(Season.SUMMER),
    SEPTEMBER(Season.AUTUMN),
    OCTOBER(Season.AUTUMN),
    NOVEMBER(Season.AUTUMN),
    DECEMBER(Season.WINTER);

    private final Season season;

    Month(Season season) {
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }
}