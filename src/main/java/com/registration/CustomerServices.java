package com.registration;

import com.dataBaseManagement.DatabaseConnectivity;
import com.reservationManagement.ReservationService;

import java.sql.*;
import java.util.Scanner;

public class CustomerServices {

    Scanner scanner = new Scanner(System.in);


    public void registerCustomer() {
        try (Connection conn = DatabaseConnectivity.connect()) {
            final String QUERY_INSERT_ACCOUNT = "INSERT INTO database.account (username, password, email, phone_number) VALUES (?, ?, ?, ?)";
            final String QUERY_INSERT_CUSTOMER = "INSERT INTO database.customer (user_id,first_name, last_name, dob, email, mobile_number, gender, country) VALUES (?,?, ?, ?, ?, ?, ?, ?)";

            System.out.println("Enter Account Details:");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            while (!ShowAdmin.isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email:");
                email = scanner.nextLine();
            }
            System.out.print("Phone Number: ");
            String phoneNumber = scanner.nextLine();
            while (!ShowAdmin.isValidPhoneNumber(phoneNumber)) {
                System.out.println("Invalid phone number format. Please enter a valid phone number:");
                phoneNumber = scanner.nextLine();
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(QUERY_INSERT_ACCOUNT)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, phoneNumber);
                preparedStatement.executeUpdate();
            }

            System.out.println("Enter Customer Details:");
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dob = scanner.nextLine();
            System.out.print("Gender: ");
            String gender = scanner.nextLine();
            System.out.print("Country: ");
            String country = scanner.nextLine();
            while (!isValidDateOfBirth(dob)) {
                System.out.println("Invalid date of birth format. Please enter a valid date of birth (YYYY-MM-DD):");
                dob = scanner.nextLine();
            }

            int userId;
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT user_id FROM database.account WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("user_id");
                } else {
                    throw new SQLException("Error retrieving user ID.");
                }
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement(QUERY_INSERT_CUSTOMER)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setDate(4, Date.valueOf(dob));
                preparedStatement.setString(5, email);
                preparedStatement.setString(6, phoneNumber);
                preparedStatement.setString(7, gender);
                preparedStatement.setString(8, country);

                preparedStatement.executeUpdate();
            }

            System.out.println("Data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean customerLogin() {
        System.out.println("Please Login to your account:");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try (Connection conn = DatabaseConnectivity.connect()) {
            String QUERY_CHECK_ACCOUNT = "SELECT * FROM database.account WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(QUERY_CHECK_ACCOUNT)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean isValidDateOfBirth(String dob) {
        String regexDateOfBirth = "^\\d{4}-\\d{2}-\\d{2}$";
        return dob.matches(regexDateOfBirth);
    }

    public void showCustomerMenu() {

        boolean isloggedIn = customerLogin();
        if (!isloggedIn) {
            System.out.println("Login failed. Exiting customer menu...");
            return;
        }
        System.out.println("Welcome to the Customer Menu:");
        System.out.println("1. Book a Ticket");
        System.out.println("2. View Booking History");
        System.out.println("3. Update Profile");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                ReservationService rs=new ReservationService();
                rs.makeReservation();
                break;
            case 2:
                fetchBookingHistory();
                break;
            case 3:
            	updateProfile();
                break;
            case 4:
                System.out.println("Exiting customer menu...");
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                break;
        }
    }

    public void fetchBookingHistory() {
        System.out.println("Enter username to fetch booking history:");
        String username = scanner.nextLine();
        int userId = getUserIdByUsername(username);

        if (userId == -1) {
            System.out.println("User not found.");
            return;
        }

        String query = "SELECT * FROM database.reservation WHERE user_id = ?";
        try (Connection conn = DatabaseConnectivity.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No booking history found for the user.");
                    return;
                }
                System.out.println("Booking History:");
                System.out.println("RESERVATION_ID | ROUTE_ID | DATE_OF_TRAVEL | NUMBER_OF_SEATS | BOOKING_TIME | BOOKING_STATUS | TOTAL_FARE");
                while (resultSet.next()) {
                    int reservationId = resultSet.getInt("RESERVATION_ID");
                    int routeId = resultSet.getInt("ROUTE_ID");
                    String dateOfTravel = resultSet.getString("DATE_OF_TRAVEL");
                    int numberOfSeats = resultSet.getInt("NUMBER_OF_SEATS");
                    Timestamp bookingTime = resultSet.getTimestamp("BOOKING_TIME");
                    String bookingStatus = resultSet.getString("BOOKING_STATUS");
                    double totalFare = resultSet.getDouble("TOTAL_FARE");
                    System.out.printf("%-14d %-9d %-15s %-16d %-23s %-15s %-9.2f\n", reservationId, routeId, dateOfTravel, numberOfSeats, bookingTime.toString(), bookingStatus, totalFare);
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while fetching booking history: " + e.getMessage());
        }
    }
    
    public void updateProfile() {
        try (Connection conn = DatabaseConnectivity.connect()) {
        	System.out.println("Enter new profile details:");
        	
        	System.out.println("Enter username to update profile details:");
        	String username = scanner.nextLine();
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Date of Birth (YYYY-MM-DD): ");
            String dob = scanner.nextLine();
            System.out.print("Gender: ");
            String gender = scanner.nextLine();
            System.out.print("Country: ");
            String country = scanner.nextLine();
            
          
            while (!isValidDateOfBirth(dob)) {
                System.out.println("Invalid date of birth format. Please enter a valid date of birth (YYYY-MM-DD):");
                dob = scanner.nextLine();
            }

            
            String updateCustomerQuery = "UPDATE database.customer SET first_name = ?, last_name = ?, dob = ?, gender = ?, country = ? WHERE user_id = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(updateCustomerQuery)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setDate(3, Date.valueOf(dob));
                preparedStatement.setString(4, gender);
                preparedStatement.setString(5, country);
                preparedStatement.setInt(6, getUserIdByUsername(username));
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Profile updated successfully!");
                } else {
                    System.out.println("Failed to update profile.");
                }
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while updating profile: " + e.getMessage());
        }
    }


    public int getUserIdByUsername(String username) {
        int userId = -1;
        try (Connection conn = DatabaseConnectivity.connect()) {
            String query = "SELECT user_id FROM database.account WHERE username = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    userId = resultSet.getInt("user_id");
                } else {
                    System.out.println("User not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }



}


