package com.solvd.agency.enums;

public enum Country {
    POLAND(Season.SUMMER),
    GERMANY(Season.SUMMER),
    LATVIA(Season.SPRING),
    CZECH_REPUBLIC(Season.SPRING),
    USA(Season.SUMMER),

    SWITZERLAND(Season.WINTER),
    NEW_ZEALAND(Season.WINTER),
    JAPAN(Season.AUTUMN),
    CANADA(Season.AUTUMN);

    private final Season bestVacationSeason;

    Country(Season bestVacationSeason) {
        this.bestVacationSeason = bestVacationSeason;
    }

    public Season getBestVacationSeason() {
        return bestVacationSeason;
    }

    @Override
    public String toString() {
        return this.name() + " Best Vacation Season: " + bestVacationSeason;
    }
}