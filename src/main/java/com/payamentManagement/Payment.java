package com.payamentManagement;


import java.sql.*;

import java.util.Scanner;
//public abstract class Payment {
//    protected double amount;
//    protected String paymentStatus;
//
//    public Payment(double amount) {
//        this.amount = amount;
//        this.paymentStatus = "Pending";
//    }
//
//    public abstract void processPayment(Connection conn);
//
//    protected void insertPaymentIntoDatabase(Connection conn, String paymentMethod) throws SQLException {
//        String sql = "INSERT INTO Payment (Amount, Payment_Method, Payment_Status) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            stmt.setDouble(1, amount);
//            stmt.setString(2, paymentMethod);
//            stmt.setString(3, paymentStatus);
//            stmt.executeUpdate();
//            ResultSet rs = stmt.getGeneratedKeys();
//            if (rs.next()) {
//                int paymentId = rs.getInt(1);
//                System.out.println("Payment ID: " + paymentId);
//            }
//        }
//    }
//}
public class Payment {
	 static Scanner scanner = new Scanner(System.in);
    public static boolean makePayment(int paymentType, double amount) {
        switch (paymentType) {
            case 1:
                return cashPayment(amount);
            case 2:
                return cardPayment(amount);
            default:
                System.out.println("Invalid payment type");
                return false;
        }
    }
    private static boolean cashPayment(double amount) {
  
        System.out.println("Cash Payment!");
        System.out.print("Enter the amount : ");
        double amountReceived = scanner.nextDouble();
        if (amountReceived >= amount) {
            double change = amountReceived - amount;
            System.out.println("Payment completed. Change: " + change);
            return true;
        } else {
            System.out.println("Insufficient amount.");
            return false;
        }
        
    }
    private static boolean cardPayment(double amount) {
        System.out.println("Card Payment!");
        System.out.print("Enter card number: ");
        String cardNumber = scanner.next();
        System.out.print("Enter expiration date (MM/YYYY): ");
        String expirationDate = scanner.next();
        System.out.print("Enter CVV: ");
        int cvv = scanner.nextInt();
        boolean isCardValid = validateCard(cardNumber, cvv);
        if (isCardValid) {
            System.out.println("Card processing successful. Payment completed.");
            return true;
        } else {
            System.out.println("Card processing failed. Payment failed.");
            return false;
        }
    }
    private static boolean validateCard(String cardNumber, int cvv) {
        return !cardNumber.isEmpty()&& cvv >= 0;
    }
}
