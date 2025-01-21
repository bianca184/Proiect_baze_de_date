package com.example.schoolmanagementsystem.model;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection() {
        String databaseName = "proiect";
        String databaseUser = "root";
        String databasePassword = "root";

        String url = "jdbc:mysql://127.0.0.1:3306/" + databaseName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            //System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found: " + e.getMessage());
        }
        return databaseLink;
    }

    public static void closeConnection() {
        if (databaseLink != null) {
            try {
                databaseLink.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
