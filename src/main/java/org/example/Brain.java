package org.example;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public abstract class Brain extends Model{
    public static User currentUser = null;

    public static void taskDeleteUser(){

        currentUser.deleteAllUserAccounts(); //Deletes all accounts if there are any

        if (currentUser.deleteThisUserInDatabase()){ //Deletes user in database
            currentUser = null; // If successfully deleted removes current user
            Gui.setAnnouncement("User has been deleted");
            System.out.println("User has been deleted");
        }


    }


    public static void createUser(String userName, String lastName, int socialSecurityNr, String password) throws SQLException {
        Connection conn = GetConnection();

        String[] hashPass = Encryptor.EncryptPassword(password);
        String time = String.valueOf(java.time.LocalDate.now());

        String query = "INSERT INTO users_swosh( name ,lastName ,socialSecurityNr ,created ,password)\n" +
                "VALUE(?,?,?,?,?)";

        PreparedStatement statement = conn.prepareStatement(query, RETURN_GENERATED_KEYS);

        statement.setString(1,userName);
        statement.setString(2,lastName);
        statement.setInt(3,socialSecurityNr);
        statement.setString(4,time);
        statement.setString(5,hashPass[0]);

        if (isUniqueSocialNr(socialSecurityNr)){ // if social number is unique, might be removed as social numbers can only be unique set when database is created
            int rowsAffected = statement.executeUpdate();
            ResultSet getMyDamnKey = statement.getGeneratedKeys(); // Returns id of new user
            if (rowsAffected > 0) {
                getMyDamnKey.next();
                int myId = getMyDamnKey.getInt(1);

                Gui.setAnnouncement("User Created!");
                System.out.println("User Created! Id = " + myId);

                login(myId); // Logs in as new user

                System.out.println(currentUser.getLastName());
            }else {
                Gui.setAnnouncement("Failed to create user");
                System.out.println("User failed to be created :/");
            }
        }
        else {
            Gui.setAnnouncement("Social number already used");
            System.out.println("Social number already used");
        }
        conn.close();
    }
    public static boolean isUniqueSocialNr(int toCheck) throws SQLException {
        Connection conn = GetConnection();
        String query = "SELECT * FROM users_swosh WHERE socialSecurityNr =?";

        //Makes sure social number is unique

        try {
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1,toCheck);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                System.out.println("Social number is not unique");
                conn.close();

                return false;

            }else {
                System.out.println("Social number is unique");
                conn.close();
                return true;
            }
        }catch (SQLException e){
            System.out.println("Wrong Security number");
            conn.close();

            return false;

        }
    }

    public static int getIdAndCheckPassword(int socialNr, String password) throws SQLException {
        Connection conn = GetConnection();
        String query = ("SELECT id, password FROM users_swosh WHERE socialSecurityNr =?");

        // When logging in this makes sure password is correct and gets id to log in

        int id;
        String hashPassword;

        try {

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1,socialNr);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                id = resultSet.getInt("id");
                hashPassword = resultSet.getString("password");

            }else {

                Gui.setAnnouncement("Didn´t find user");
                throw new SQLException("No user of this found");
            }

            if (Encryptor.Verify(password,hashPassword)){ // Verifies received password
                conn.close();
                return id;
            }
            else {
                Gui.setAnnouncement("Wrong password");
                System.out.println("Wrong password");
            }

        }catch (SQLException e){
            Gui.setAnnouncement("Wrong security number probably");
            System.out.println("Wrong Security number");
        }
        conn.close();
        return 0;
    }
    public static void login(int id) {
        try {
            currentUser = new User(id);
            UserBoard.updateUserBoard();

        }catch (SQLException e){
            Gui.setAnnouncement("Failed to login");
            System.out.println("failed to login");
        }
    }
}
