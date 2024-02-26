package com.busManagement;


import com.dataBaseManagement.DatabaseConnectivity;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bus
{
    Scanner scanner = new Scanner(System.in);


    public void viewBuses()
    {

        String selectQuery = "SELECT b.BUSNAME, b.BUSCODE, b.BUSTYPE, b.AVAILABLESEATS, b.SEATPRICE, r.ORIGIN_CITY, " +
                "r.DESTINATION_CITY, r.DEPARTURE_TIME, r.TRAVEL_DURATION FROM database.bus b JOIN database.route r ON b.ROUTE_ID = r.ROUTE_ID ORDER BY b.BUSID";

        try (Connection connection = DatabaseConnectivity.connect();
             PreparedStatement ps = connection.prepareStatement(selectQuery);
             ResultSet result = ps.executeQuery()) {
            System.out.println("----------------------------------");
            System.out.println("Buses are available here are!!!");
            System.out.println("----------------------------------");
            while (result.next()) {
              //  int busId = result.getInt("b.BUSID");
                String busCode = result.getString("BUSCODE");
                String busName = result.getString("BUSNAME");
                String busType = result.getString("BUSTYPE");
                int seatsAvailable = result.getInt("AVAILABLESEATS");
                double seatPrice = result.getDouble("SEATPRICE");
                String originCity = result.getString("ORIGIN_CITY");
                String destinationCity = result.getString("DESTINATION_CITY");
                Timestamp departureTime = result.getTimestamp("DEPARTURE_TIME");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fdt= sdf.format(departureTime);

                String travelDuration = result.getString("TRAVEL_DURATION");

                System.out.println("Bus Code: " + busCode);
                System.out.println("Bus Name: " + busName);
                System.out.println("Bus Type: " + busType);
                System.out.println("Seats Available: " + seatsAvailable);
                System.out.println("Seat Price: " + seatPrice);
                System.out.println("Origin City: " + originCity);
                System.out.println("Destination City: " + destinationCity);
                System.out.println("Departure Time: " + departureTime);
                System.out.println("Travel Duration: " + travelDuration);
                System.out.println("----------------------------------");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }









    void addBus()
    {
        try
        {
            System.out.println("Enter bus code: ");
            String busCode = scanner.next();

            System.out.println("Enter bus name: ");
            String busName = scanner.next();

            boolean checkInput = false;
            String busType = "";
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter bus type : ");
                    busType = scanner.nextLine();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for bus type ID.");
                    scanner.next();
                }
            }
            checkInput = false;
            scanner.nextLine();
            int busOperatorId = 0;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter bus operatorId");
                    busOperatorId = scanner.nextInt();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for bus operator ID.");
                    scanner.next(); // Consume the invalid input
                }
            }

            int routeId = 0;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter route ID: ");
                    routeId = scanner.nextInt();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for route ID.");
                    scanner.next();
                }
            }



            int totalSeats = 0;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter total seats: ");
                    totalSeats = scanner.nextInt();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for total seats.");
                    scanner.next();
                }
            }

            double seatPrice = 0.0;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter seat price: ");
                    seatPrice = scanner.nextDouble();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid double for seat price.");
                    scanner.next();
                }
            }

            int isActive = 0;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter availability status (1 for active, 0 for inactive): ");
                    isActive = scanner.nextInt();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for availability status.");
                    scanner.next();
                }
            }

            int availableSeats = 0;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter available seats: ");
                    availableSeats = scanner.nextInt();
                    checkInput = true;
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Invalid input. Please enter a valid integer for available seats.");
                    scanner.next();
                }
            }

            LocalDate startDate = null;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter start date (YYYY-MM-DD): ");
                    String startDateStr = scanner.next();
                    startDate = LocalDate.parse(startDateStr);
                    checkInput = true;
                }
                catch (DateTimeParseException e)
                {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }

            LocalDate endDate = null;
            checkInput = false;
            while (!checkInput)
            {
                try
                {
                    System.out.println("Enter end date (YYYY-MM-DD): ");
                    String endDateStr = scanner.next();
                    endDate = LocalDate.parse(endDateStr);
                    checkInput = true;
                }
                catch (DateTimeParseException e)
                {
                    System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                }
            }

            String insertQuery = "INSERT INTO DATABASE.Bus (busCode, busName, busOperatorid, route_id, busType, totalSeats, isActive, seatPrice, availableSeats, startDate, endDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            Connection connection = DatabaseConnectivity.connect();

            try
            {
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setString(1, busCode);
                ps.setString(2, busName);
                ps.setInt(3, busOperatorId);
                ps.setInt(4, routeId);
                ps.setString(5, busType);
                ps.setInt(6, totalSeats);
                ps.setInt(7, isActive);
                ps.setDouble(8, seatPrice);
                ps.setInt(9, availableSeats);
                ps.setDate(10, Date.valueOf(startDate));
                ps.setDate(11, Date.valueOf(endDate));

                int rowsInserted = ps.executeUpdate();

                if (rowsInserted > 0)
                {
                    System.out.println("Bus added successfully.");
                }
                else
                {
                    System.out.println("Failed to add bus.");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }





    void deleteBus() {
        try {
            System.out.println("*");
            System.out.println("Enter bus ID to make the bus unavailable ");
            int busId = scanner.nextInt();

            String updateQuery = "UPDATE database.Bus SET isActive = 1 WHERE busid = ?";

            try (Connection connection = DatabaseConnectivity.connect();
                 PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setInt(1, busId);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Bus status updated successfully. Bus is now inactive.");
                } else {
                    System.out.println("Failed to update bus status. Bus not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }




