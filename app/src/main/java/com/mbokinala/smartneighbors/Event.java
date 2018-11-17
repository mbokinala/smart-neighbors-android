package com.mbokinala.smartneighbors;

public class Event {
    public String name, place, neighborID, neighborName, status, date, category, notes;

    public Event(String name, String place , String neighborID, String status, String neighborName, String date, String category, String notes) {
        this.name = name;
        this.place = place;
        this.neighborID = neighborID;
        this.status = status;
        this.neighborName = neighborName;
        this.date = date;
        this.category = category;
        this.notes = notes;
    }
}
