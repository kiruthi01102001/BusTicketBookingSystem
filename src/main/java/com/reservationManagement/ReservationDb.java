package com.reservationManagement;

import com.dataBaseManagement.DatabaseConnectivity;
import com.payamentManagement.Payment;
import com.registration.Admin;
import com.registration.ShowAdmin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;


public class ReservationDb {


        Scanner scanner=new Scanner(System.in);
         boolean flag=false;

    public void addReservation(Reservation reservation) {
        final String QUERY_INSERT = "INSERT INTO database.Reservation (Route_ID, Date_of_Travel, User_ID, Number_of_Seats, Booking_Time, Payment_ID, bus_Id, total_fare) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        final String QUERY_SELECT_USERID = "SELECT User_ID FROM database.account WHERE UserName = ?";
        final String QUERY_SELECT_RESERVATION_ID = "SELECT Reservation_ID FROM database.Reservation WHERE Route_ID = ? AND Date_of_Travel = ? AND User_ID = ? AND Number_of_Seats = ?";
        final String QUERY_SELECT_BOOKING_STATUS = "SELECT Booking_Status FROM database.Reservation WHERE Reservation_ID = ?";


        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(QUERY_INSERT);
             PreparedStatement stmtUserId = conn.prepareStatement(QUERY_SELECT_USERID);
             PreparedStatement stmtReservationId = conn.prepareStatement(QUERY_SELECT_RESERVATION_ID);
             PreparedStatement stmtBookingStatus = conn.prepareStatement(QUERY_SELECT_BOOKING_STATUS)){

            // Check if the bus is active
            boolean isBusActive = checkBusStatus(reservation.getBusId());

            if (!isBusActive) {
                System.out.println("The bus is currently inactive. Reservation cannot be made.");
                return;
            }

            // Retrieve User_ID based on UserName
            stmtUserId.setString(1, reservation.getUserName());
            try (ResultSet rsUserId = stmtUserId.executeQuery()) {
                if (rsUserId.next()) {
                    int userId = rsUserId.getInt("User_ID");
                    reservation.setUserId(userId);
                } else {
                    System.out.println("User not found with the provided UserName.");
                    return; // Exit method if user not found
                }
            }

            preparedStatement.setInt(1, reservation.getRouteId());
            preparedStatement.setObject(2, reservation.getDateOfTravel());
            preparedStatement.setInt(3, reservation.getUserId()); // Set User_ID obtained from UserName
            preparedStatement.setInt(4, reservation.getNumberOfSeats());
            preparedStatement.setObject(5, LocalDateTime.now()); // Booking Time
            preparedStatement.setInt(6, reservation.getPaymentId());
            preparedStatement.setInt(7, reservation.getBusId());

            double totalFare = calculateTotalFare(reservation);
            double roundedTotalFare = Math.round(totalFare * 100.0) / 100.0;
            preparedStatement.setDouble(8, roundedTotalFare);

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("Booking has been processed. ");
                
               
               
                // Retrieve Reservation_ID
                stmtReservationId.setInt(1, reservation.getRouteId());
                stmtReservationId.setObject(2, reservation.getDateOfTravel());
                stmtReservationId.setInt(3, reservation.getUserId());
                stmtReservationId.setInt(4, reservation.getNumberOfSeats());

                int reservationId=0;
                try (ResultSet rsReservationId = stmtReservationId.executeQuery()) {
                    if (rsReservationId.next()) {
                         reservationId = rsReservationId.getInt("Reservation_ID");
                        ShowAdmin.approveBooking(reservationId);
                    } else {
                        System.out.println("Failed to retrieve the generated reservation ID.");
                    }
                }
                String bookingStatus="";
                stmtBookingStatus.setInt(1, reservationId);
                
                try (ResultSet rsBookingStatus = stmtBookingStatus.executeQuery()) {
                    if (rsBookingStatus.next()) {
                        bookingStatus = rsBookingStatus.getString("Booking_Status");
                       
                       
                    } else {
                        System.out.println("Failed to retrieve the booking status.");
                    }
                }

                updateAvailableSeats(reservation.getBusId(), reservation.getNumberOfSeats(), bookingStatus);
            } else {
                System.out.println("Please try after some time!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public double calculateTotalFare( Reservation reservation) {
        final String QUERY_SELECT = "SELECT SEATPRICE FROM database.bus WHERE busId = ?";
        double totalFare ;

        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(QUERY_SELECT)) {

            preparedStatement.setInt(1, reservation.getBusId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    double seatPrice = resultSet.getDouble("SEATPRICE");
                    totalFare = seatPrice * reservation.getNumberOfSeats();
                } else {
                   
                    throw new RuntimeException("No seat price found for busId: " + reservation.getBusId());
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return totalFare;
    }




        public void  viewAllReservations() {
            try {
                
                Connection conn = DatabaseConnectivity.connect();
                
                String sql = "SELECT * FROM database.Reservation ORDER BY Reservation_ID DESC";


               
                PreparedStatement statement = conn.prepareStatement(sql);

                
                ResultSet resultSet = statement.executeQuery();

               
                while (resultSet.next()) {
                    int reservationId = resultSet.getInt("Reservation_ID");
                    int routeId = resultSet.getInt("Route_ID");
                    Date dateOfTravel = resultSet.getDate("Date_of_Travel");
                    int userId = resultSet.getInt("User_ID");
                    int numberOfSeats = resultSet.getInt("Number_of_Seats");
                    Timestamp bookingTime = resultSet.getTimestamp("Booking_Time");
                    int paymentId = resultSet.getInt("Payment_ID");
                    int busId = resultSet.getInt("Bus_ID");
                    String bookingStatus = resultSet.getString("Booking_Status");
                    double totalFare = resultSet.getDouble("Total_Fare");

                    
                    System.out.println("Reservation ID: " + reservationId);
                    System.out.println("Route ID: " + routeId);
                    System.out.println("Date of Travel: " + dateOfTravel);
                    System.out.println("User ID: " + userId);
                    System.out.println("Number of Seats: " + numberOfSeats);
                    System.out.println("Booking Time: " + bookingTime);
                    System.out.println("Payment ID: " + paymentId);
                    System.out.println("Bus ID: " + busId);
                    System.out.println("Booking Status: " + bookingStatus);
                    System.out.println("Total Fare: " + totalFare);
                    System.out.println();
                }

                
                resultSet.close();
                statement.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    public boolean cancelReservation() {
        System.out.println("Enter Reservation ID:");
        int reservationId = 0;
        try {
            reservationId = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Input mismatch. Please enter a valid integer.");
            return false;
        }

        final String QUERY_UPDATE_RESERVATION = "UPDATE database.Reservation SET Booking_Status = 'cancelled' WHERE Reservation_ID = ?";
        final String QUERY_SELECT_RESERVATION = "SELECT Bus_ID, Number_of_Seats FROM database.Reservation WHERE Reservation_ID = ?";

        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement updateReservationStatement = conn.prepareStatement(QUERY_UPDATE_RESERVATION);
             PreparedStatement selectReservationStatement = conn.prepareStatement(QUERY_SELECT_RESERVATION)) {

           
            updateReservationStatement.setInt(1, reservationId);
            int rowsUpdated = updateReservationStatement.executeUpdate();

            if (rowsUpdated > 0) {
                
                selectReservationStatement.setInt(1, reservationId);
                ResultSet rs = selectReservationStatement.executeQuery();

                if (rs.next()) {
                    int busId = rs.getInt("Bus_ID");
                    int numberOfSeats = rs.getInt("Number_of_Seats");

                   
                    updateAvailableSeats(busId, numberOfSeats, "cancelled");

                    System.out.println("Reservation with ID " + reservationId + " cancelled successfully.");
                    return true;
                } else {
                    System.out.println("No reservation found with ID " + reservationId + ". No changes were made.");
                    return false;
                }
            } else {
                System.out.println("No reservation found with ID " + reservationId + ". No changes were made.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while cancelling the reservation.");
            e.printStackTrace();
            return false;
        }
    }


    private boolean checkBusStatus(int busId) {
        boolean isActive = false;
        String QUERY_CHECK_STATUS = "SELECT ISACTIVE FROM database.Bus WHERE BUSID = ?";

        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(QUERY_CHECK_STATUS)) {

            preparedStatement.setInt(1, busId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isActive = resultSet.getInt("ISACTIVE") == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isActive;
    }


    public void updateAvailableSeats(int busId, int numberOfSeats, String bookingStatus) {
         String QUERY_UPDATE="";


        if ("approved".equalsIgnoreCase(bookingStatus))
            QUERY_UPDATE = "UPDATE database.Bus SET AvailableSeats = AvailableSeats - ? WHERE BusId = ?";
        else if ("cancelled".equalsIgnoreCase(bookingStatus)){
            QUERY_UPDATE = "UPDATE database.Bus SET AvailableSeats = AvailableSeats + ? WHERE BusId = ?";
        }
        else {
        	System.out.print("Error");
        }

        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(QUERY_UPDATE)) {

            preparedStatement.setInt(1, numberOfSeats);
            preparedStatement.setInt(2, busId);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Available seats for Bus ID " + busId + " updated successfully.");
            } else {
                System.out.println("Failed to update available seats for Bus ID " + busId + ".");
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while updating available seats for Bus ID " + busId + ".");
            e.printStackTrace();
        }
    }


}





