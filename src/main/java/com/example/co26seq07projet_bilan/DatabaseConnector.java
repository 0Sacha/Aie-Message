package com.example.co26seq07projet_bilan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://sql3.minestrator.com:3306/minesr_267dViKu";
    private static final String USER = "minesr_267dViKu";
    private static final String PASSWORD = "1oH5EfW7CJQTHh9X";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // <- IMPORTANT
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL introuvable !");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
