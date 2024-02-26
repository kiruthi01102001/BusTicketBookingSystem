package com.registration;

import com.busManagement.BusManagement;
import com.dataBaseManagement.DatabaseConnectivity;
import com.busRouteManagement.BusRouteManagement;
import com.reservationManagement.ReservationDb;
import com.reservationManagement.ReservationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowAdmin {
    Scanner scanner=new Scanner(System.in);

    public boolean loggedIn = false;
    public boolean adminLogin() {
      //  boolean isLoggedIn = false;
        System.out.println("Please Login to your account !!!");
        System.out.println("Enter your UserName :");
        String admin_name = scanner.next();
        if(!isValidUsername(admin_name)){
            System.out.println("Invalid Username!!! Username must contain only letters, digits, or underscores.");
            return false;
        }


        System.out.println("Enter your Password :");
        String admin_password = scanner.next();
        if(!isValidPassword(admin_password)){
            System.out.println("Invalid Password!!! Password must contain at least 8 characters, " +
                    "including at least one letter, one digit, and one special character from @$!%?&");
            return false;

        }

        try (Connection con = DatabaseConnectivity.connect()) {
            String selectQuery = "SELECT * FROM database.admin WHERE admin_name = ? AND admin_password = ?";
            try (PreparedStatement ps = con.prepareStatement(selectQuery)) {
                ps.setString(1, admin_name);
                ps.setString(2, admin_password);

                try (ResultSet result = ps.executeQuery()) {
                    if (result.next()) {
                        System.out.println("-----Login successful!-----");
                        loggedIn = true;
                        return true;
                    } else {
                        System.out.println("Invalid username or password!");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public void accountRecovery(){
        System.out.println("Enter your UserName :");
        String username = scanner.next();
        System.out.println("Enter new Password :");
        String newPassword = scanner.next();
        System.out.println("Confirm new Password :");
        String confirmPassword = scanner.next();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try again.");
            return;
        }

        try (Connection con = DatabaseConnectivity.connect()) {
            String updateQuery = "UPDATE database.admin SET admin_password = ? WHERE admin_name = ?";
            try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
                ps.setString(1, newPassword);
                ps.setString(2, username);
                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Admin password updated successfully!");
                } else {
                    System.out.println("Admin account not found.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void displayAdminMenu() {
        boolean loggedIn = adminLogin(); // Check login status

        if (!loggedIn) {
            System.out.println("Login failed. Exiting...");
            return;
        }




        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Profile Management");
            System.out.println("2. Bus Management");
            System.out.println("3. Bus Route Management");
            System.out.println("4. Reservation Management");
            System.out.println("5. View Booking History");
            System.out.println("6. Go Back");
            System.out.print("Enter your choice: ");

            boolean checkInput = false;
            int choice = 0;
            while (!checkInput) {
                try {
                    System.out.println("Enter your choice:");
                    choice = scanner.nextInt();
                    checkInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer ");
                    scanner.next();
                }
            }

            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Profile Management");
                    System.out.println("Do you want to Update password? Enter 1 for yes, 0 for No:");
                    int getChoice = scanner.nextInt();
                    if (getChoice == 1) {
                        accountRecovery();
                    }
                    break;
                case 2:
                    BusManagement bm = new BusManagement();
                    break;
                case 3:
                    BusRouteManagement brm = new BusRouteManagement();
                    break;
                case 4:
                    ReservationService rs = new ReservationService();
                    break;
                case 5:
                    ReservationDb rdb = new ReservationDb();
                    rdb.viewAllReservations();
                    break;
                case 6:

                    return;
                case 7:
                    System.exit(0);
                    System.out.println("Exiting program...");

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 to 6.");
            }
        }
    }




    public static boolean isValidUsername(String userName)
    {
        String regexUserName = "^[A-Za-z0-9_]+$";
        Pattern pattern = Pattern.compile(regexUserName);
        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password)
    {
        String regexPassword = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%*?&]{8,}$";

        Pattern pattern = Pattern.compile(regexPassword);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean  isValidEmail(String email)
    {
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber)
    {
        String regexPhoneNumber = "^[6-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regexPhoneNumber);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }


        public static boolean approveBooking(int reservationId) {
            final String QUERY_UPDATE = "UPDATE database.Reservation SET Booking_Status = 'approved' WHERE Reservation_ID = ?";

            try (Connection conn = DatabaseConnectivity.connect();
                 PreparedStatement preparedStatement = conn.prepareStatement(QUERY_UPDATE)) {

                preparedStatement.setInt(1, reservationId);
                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Booking with ID " + reservationId + " has been approved successfully.");
                    return true;
                } else {
                    System.out.println("No booking found with ID " + reservationId + ". No changes were made.");
                    return false;
                }

            } catch (SQLException e) {
                System.out.println("An error occurred while approving the booking.");
                e.printStackTrace();
                return false;
            }
        }
    }



