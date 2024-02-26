package com.dataBaseManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





public class DatabaseConnectivity {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "SYSTEM";
    private static final String PASSWORD = "kiruthika";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle or log ClassNotFoundException
            throw new SQLException("Database driver not found", e);
        }
    }
}




