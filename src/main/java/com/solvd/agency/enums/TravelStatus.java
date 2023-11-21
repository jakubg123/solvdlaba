package com.solvd.agency.enums;

public enum TravelStatus {

    FINISHED,
    IN_PROGRESS,
    NOT_STARTED;


    public String getStatus(){
        return this.name();
    }

}
