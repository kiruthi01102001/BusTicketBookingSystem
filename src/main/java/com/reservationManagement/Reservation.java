package com.reservationManagement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int routeId;
    private LocalDate dateOfTravel;
    private String username;
    private int numberOfSeats;
    private int paymentId;

    private int busId;

    private static String bookingStatus;

    private double totalFare;

    private int userId;




    public Reservation(int reservationId, int routeId, LocalDate dateOfTravel, String username, int numberOfSeats, int paymentId, int busId, double totalFare) {
        this.reservationId = reservationId;
        this.routeId = routeId;
        this.dateOfTravel = dateOfTravel;
        this.username = username;
        this.numberOfSeats = numberOfSeats;
        this.paymentId = paymentId;
        this.busId=busId;
        this.totalFare=totalFare;
    }

    // Getters
    public int getReservationId() {
        return reservationId;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getUserId() {
        return routeId;
    }

    public LocalDate getDateOfTravel() {
        return dateOfTravel;
    }

    public String getUserName() {
        return username;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getBusId() {
        return busId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    public double  getTotalFare() {
        return totalFare;
    }



    // Factory method to create Reservation object
    public static Reservation createReservation(int reservationId, int routeId, LocalDate dateOfTravel, String username, int numberOfSeats, int paymentId, int busId, double totalFare) {
        return new Reservation(reservationId, routeId, dateOfTravel, username, numberOfSeats, paymentId,busId,totalFare);
    }


}
