package com.busRouteManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
public class BusRoute {
    private int routeId;
    private String originCity;

    private String destinationCity;
    private double travelDuration;
 //   private LocalDateTime departureTime;

 //   private Bus Bus;

    public BusRoute(int routeId,String originCity, String destinationCity, double travelDuration) {
        this.routeId=routeId;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.travelDuration = travelDuration;
 //       this.departureTime = departureTime;
    }

    public int getRouteId() {
        return routeId;
    }
    public String getOriginCity() {
        return originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public double getTravelDuration() {
        return travelDuration;
    }

//    public LocalDateTime getDepartureTime() {
//        return departureTime;
//    }

    // Setters

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public void setTravelDuration(double travelDuration) {
        this.travelDuration = travelDuration;
    }

//    public void setDepartureTime(LocalDateTime departureTime) {
//        this.departureTime = departureTime;
//    }
    
    
    
    public String toString() {
        return String.format("Route ID: %d, Origin City: %s, Destination City: %s, Travel Duration: %.2f hours",
                routeId, originCity, destinationCity, travelDuration);
    }
}


