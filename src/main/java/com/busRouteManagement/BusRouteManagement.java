package com.busRouteManagement;

import com.dataBaseManagement.DatabaseConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BusRouteManagement {
    Scanner scanner=new Scanner(System.in);
    List<BusRoute> busRoutes = new ArrayList<>();
    public BusRouteManagement() {
        while (true)
        {
            System.out.println("*");
            System.out.println("BusRoute Management");
            System.out.println("1. Add BusRoutes");
            System.out.println("2. View BusRoutes");
            System.out.println("3. Update BusRoutes");
            System.out.println("4. Remove BusRoutes");
            System.out.println("5. Go back");
            int choice = scanner.nextInt();

            switch (choice)
            {
                case 1:
                     addBusRoutes();
                    break;
                case 2:
                     viewBusRoutes();

                    break;
                case 3:
                     updateBusRoutes();

                    break;
                case 4:
                     removeBusRoutes();

                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

    }
    private void addBusRoutes() {
        System.out.println("Enter route id:");
        int routeId = scanner.nextInt();

        System.out.println("Enter origin city:");
        String originCity = scanner.next();

        System.out.println("Enter destination city:");
        String destinationCity = scanner.next();

        System.out.println("Enter travel duration (in hours):");
        double travelDuration = scanner.nextDouble();

//        System.out.println("Enter departure time (YYYY-MM-dd HH:mm):");
//        String departureTimeString = scanner.next() + " " + scanner.next();
//        LocalDateTime departureTime = LocalDateTime.parse(departureTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


        BusRoute br = new BusRoute(routeId,originCity, destinationCity, travelDuration);
        busRoutes.add(br);


        addBusRouteToDatabase(br);
    }

    private void viewBusRoutes() {
        System.out.println("BusRoutes List:");
        for (BusRoute br : busRoutes) {
            System.out.println(br.toString());
        }
    }

    private void addBusRouteToDatabase(BusRoute br) {
        String insertQuery = "INSERT INTO database.ROUTE (ROUTE_ID, ORIGIN_CITY, DESTINATION_CITY,  TRAVEL_DURATION) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectivity.connect();
             PreparedStatement ps = connection.prepareStatement(insertQuery)) {

            ps.setInt(1, br.getRouteId());
            ps.setString(2, br.getOriginCity());
            ps.setString(3, br.getDestinationCity());
  //          ps.setTimestamp(4, java.sql.Timestamp.valueOf(br.getDepartureTime()));
            ps.setString(4, String.valueOf(br.getTravelDuration()));



            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("BusRoute added to the database successfully!");
            } else {
                System.out.println("Failed to add BusRoute to the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error executing SQL statement: " + e.getMessage());
        }
    }
    private void removeBusRoutes() {

        System.out.println("Enter route id to remove ");
        int routeId=scanner.nextInt();
        try (Connection connection = DatabaseConnectivity.connect();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM database.ROUTE WHERE ROUTE_ID = ?")) {

            ps.setInt(1, routeId);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("BusRoute with route id " + routeId + " removed from the database.");
            } else {
                System.out.println("Failed to remove BusRoute with route id " + routeId + " from the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error executing SQL statement: " + e.getMessage());
        }
    }

    private void updateBusRoutes() {
        System.out.println("Enter route id to update:");
        int routeIdToUpdate = scanner.nextInt();


        BusRoute routeToUpdate = findBusRouteById(routeIdToUpdate);
        if (routeToUpdate == null) {
            System.out.println("BusRoute with route id " + routeIdToUpdate + " not found.");
            return;
        }


        System.out.println("Updating BusRoute with route id " + routeIdToUpdate + ". Current details:");
        System.out.println(routeToUpdate.toString());


        System.out.println("Enter new origin city:");
        String newOriginCity = scanner.next();

        System.out.println("Enter new destination city:");
        String newDestinationCity = scanner.next();

        System.out.println("Enter new travel duration (in hours):");
        double newTravelDuration = scanner.nextDouble();

//        System.out.println("Enter new departure time (YYYY-MM-dd HH:mm):");
//        String newDepartureTimeString = scanner.next() + " " + scanner.next();
//        LocalDateTime newDepartureTime = LocalDateTime.parse(newDepartureTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


        routeToUpdate.setOriginCity(newOriginCity);
        routeToUpdate.setDestinationCity(newDestinationCity);
        routeToUpdate.setTravelDuration(newTravelDuration);
 //       routeToUpdate.setDepartureTime(newDepartureTime);

        System.out.println("BusRoute with route id " + routeIdToUpdate + " updated successfully!");
    }
    private BusRoute findBusRouteById(int routeId) {
        for (BusRoute route : busRoutes) {
            if (route.getRouteId() == routeId) {
                return route;
            }
        }
        return null;
    }

}






