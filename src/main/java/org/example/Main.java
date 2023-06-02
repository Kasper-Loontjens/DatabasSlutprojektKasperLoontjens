package org.example;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Hello world!");

        Model.initializeConnection(); // Starts my connection to desired SQL Database

        CheckCreateTables.taskCreateTable(); // Checks if the tables needed exist, if they don´t they are created

        Gui gui = new Gui(); // Creates my Graphic interface

    }
}