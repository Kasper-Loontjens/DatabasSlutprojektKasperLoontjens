package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Model {
    static MysqlDataSource dataSource;
    static String url = "localhost";
    static int port = 3306;
    static String database = "slutprojekt";
    static String username = "root";
    static String password = "";

    public static void initializeConnection(){

        // Starts my connection to desired SQL Database
        try {
            System.out.printf("Configuring data source...");
            dataSource = new MysqlDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setUrl("jdbc:mysql://" + url + ":" + port + "/" + database + "?serverTimezone=UTC");
            dataSource.setUseSSL(false);
            System.out.printf("done!\n");

        }
        catch(SQLException e){
            System.out.printf("failed!\n");
            System.exit(0);
        }
    }
    protected static Connection GetConnection(){

        //Gets new connection

        try{
            System.out.printf("Fetching connection to database...");
            Connection connection = dataSource.getConnection();
            System.out.printf("done!\n");
            return connection;
        }
        catch(SQLException e){
            System.out.printf("failed!\n");
            //PrintSQLException(e);
            System.exit(0);
            return null;
        }
    }

}
