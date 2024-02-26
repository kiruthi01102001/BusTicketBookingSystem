package com.reservationManagement;

import com.dataBaseManagement.DatabaseConnectivity;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class BusSearchMenu {
    private Scanner scanner = new Scanner(System.in);

    public BusSearchMenu() {
        scanner = new Scanner(System.in);
        // Display menu
        System.out.println("Welcome to Bus Search Menu");
        System.out.println("1. Search by Origin and Destination");
        System.out.println("2. Search by searchByRoute And AvailableSeats");
        System.out.println("3. Search by Date of Travel");
        System.out.println("4. Exit");

        // Process user choice
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:

                List<List<String>> list=searchByOriginAndDestination();
                printResultSet(list);
                break;
            case 2:

                List<List<String>> list1=searchByRouteAndAvailableSeats();
                printResultSet(list1);
                break;
            case 3:
                System.out.print("Enter date of travel (YYYY-MM-DD): ");
                String dateOfTravel = scanner.nextLine().trim();
                List<List<String>> list2=searchByDateOfTravel(dateOfTravel);
                printResultSet(list2);
                break;
            case 4:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
                System.exit(1);
                break;
        }
    }


    public List<List<String>> searchByOriginAndDestination()  {
        System.out.print("Enter Origin City: ");
        String originCity = scanner.nextLine().trim();
        System.out.print("Enter Destination City: ");
        String destinationCity = scanner.nextLine().trim();

        List<List<String>> resultSet = new ArrayList<>();
        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM database.bus WHERE ROUTE_ID IN " +
                             "(SELECT ROUTE_ID FROM database.Route WHERE ORIGIN_CITY = ? AND DESTINATION_CITY = ?)")) {
            statement.setString(1, originCity);
            statement.setString(2, destinationCity);
            try (ResultSet rs = statement.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names as first row
                List<String> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                resultSet.add(columnNames);

                // Add data rows
                while (rs.next()) {
                    List<String> rowData = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(rs.getString(i));
                    }
                    resultSet.add(rowData);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }


    public List<List<String>> searchByRouteAndAvailableSeats() {

        List<List<String>> resultSet = new ArrayList<>();

        System.out.print("Enter Origin City: ");
        String originCity = scanner.nextLine().trim();
        System.out.print("Enter Destination City: ");
        String destinationCity = scanner.nextLine().trim();
        System.out.print("Enter minimum available seats: ");
        int minAvailableSeats = scanner.nextInt();

        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM database.bus b " +
                             "INNER JOIN database.Route r ON b.ROUTE_ID = r.ROUTE_ID " +
                             "WHERE r.ORIGIN_CITY = ? AND r.DESTINATION_CITY = ? " +
                             "AND b.AVAILABLESEATS >= ?")) {
            statement.setString(1, originCity);
            statement.setString(2, destinationCity);
            statement.setInt(3, minAvailableSeats);
            try (ResultSet rs = statement.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names as first row
                List<String> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                resultSet.add(columnNames);

                // Add data rows
                while (rs.next()) {
                    List<String> rowData = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(rs.getString(i));
                    }
                    resultSet.add(rowData);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }


    public List<List<String>> searchByDateOfTravel(String dateOfTravel) {
        List<List<String>> resultSet = new ArrayList<>();
        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM database.bus WHERE TO_DATE(?, 'YYYY-MM-DD') BETWEEN STARTDATE AND ENDDATE")) {
            statement.setString(1, dateOfTravel);
            try (ResultSet rs = statement.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names as first row
                List<String> columnNames = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.add(metaData.getColumnName(i));
                }
                resultSet.add(columnNames);

                // Add data rows
                while (rs.next()) {
                    List<String> rowData = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.add(rs.getString(i));
                    }
                    resultSet.add(rowData);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }



    public void printResultSet(List<List<String>> resultSet) {
        if (resultSet.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        // Print column names
        List<String> columnNames = resultSet.get(0);
        for (String columnName : columnNames) {
            System.out.printf("%-20s", columnName);
        }
        System.out.println();

        // Print data rows
        for (int i = 1; i < resultSet.size(); i++) {
            List<String> rowData = resultSet.get(i);
            for (String value : rowData) {
                System.out.printf("%-20s", value);
            }
            System.out.println();
        }
    }

}
