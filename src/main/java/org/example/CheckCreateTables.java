package org.example;

import java.sql.*;

public abstract class CheckCreateTables extends Model{
    static String queryUsers = ("CREATE TABLE users_Swosh (\n" +
            "\tid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "\tname VARCHAR(50),\n" +
            "\tlastName VARCHAR(50),\n" +
            "\tsocialSecurityNr INT UNIQUE NOT NULL,\n" +
            "\tcreated DATE,\n" +
            "\tpassword VARCHAR(255))");
    static String queryAccount = ("CREATE TABLE account_Swosh (\n" +
            "\tid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "\tuser_id INT,\n" +
            "\tvalue INT,\n" +
            "\taccountNR INT UNIQUE NOT NULL,\n" +
            "\tcreated DATE)");
    static String queryTransaction = ("CREATE TABLE transaction_Swosh (\n" +
            "\tid INT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "\tuser_id INT,\n" +
            "\treceiver_id INT,\n" +
            "\taccount_id INT,\n" +
            "\treceiver_account_Nr INT,\n" +
            "\tvalue INT,\n" +
            "\tcreated DATE)");

    public static void taskCreateTable(){
        try {
            createTable(queryUsers, "users_Swosh");
            createTable(queryAccount,"account_Swosh");
            createTable(queryTransaction, "transaction_Swosh");
        }catch (SQLException e){
            System.out.println("boi");
        }

    }
    public static void createTable(String query,String table) throws SQLException {
        Connection conn = GetConnection();
        Statement statement = conn.createStatement();

        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        if (tables.next()) {
            // Table already exist
            System.out.println(table+" table exist");
        }
        else {
            // Table does not exist and will be created
            System.out.println(table+" don´t exist and will be created");
            int result = statement.executeUpdate(query);
            System.out.println("Result: " + result);
        }
        conn.close();
    }

}
