package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class User extends Model{
    protected int id;
    protected String name;
    protected String lastName;
    protected int socialSecurityNr;
    protected String created;
    protected String hashedPassword;

    public ArrayList<Account> accounts = new ArrayList<>();

    //public ArrayList<Transaction> sentTransactions = new ArrayList<>();

    public ArrayList<Transaction> searchedTransactions = new ArrayList<>();

    User(int id) throws SQLException {

        // This saves a model of user from database to be used

        Connection conn = GetConnection();

        String query = "SELECT * FROM users_Swosh WHERE id =?";

        try {

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1,id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                this.id = id;
                this.name = resultSet.getString("name");
                this.lastName = resultSet.getString("lastName");
                this.socialSecurityNr = resultSet.getInt("socialSecurityNr");
                this.created = resultSet.getString("created");
                this.hashedPassword = resultSet.getString("password");
            }else {
                throw new SQLException("No user of this found");
            }

        }catch (SQLException e){

        }
        conn.close();
        getAllMyAccountsFromDatabase(); // Gets all accounts and saves in a list

    }

    public boolean deleteThisUserInDatabase() {
        String query2 =  "DELETE FROM users_swosh WHERE id = ?;";

        try {
            Connection conn = GetConnection();

            PreparedStatement statement2 = conn.prepareStatement(query2);

            statement2.setInt(1,id);

            int rowsUpdated2 = statement2.executeUpdate();
            if (rowsUpdated2 == 0) {
                conn.close();
                throw new SQLException("Failed to delete user");
            }else {
                conn.close();
                Gui.setAnnouncement("User has been deleted");
                return true;
            }

        } catch (SQLException e) {
            Gui.setAnnouncement("Failed to delete user");
            System.out.println("Failed to delete user");
            return false;
        }
    }

    public boolean deleteAllUserAccounts(){
        String query =  "DELETE FROM account_swosh WHERE user_id = ?;";

        try {
            Connection conn = GetConnection();

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1,id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                conn.close();
                throw new SQLException("Failed to delete accounts");
            }else {
                Gui.setAnnouncement("Accounts has been deleted");
                System.out.println("Accounts deleted");
                conn.close();
                return true;
            }

        } catch (SQLException e) {
            Gui.setAnnouncement("Accounts has been deleted");
            System.out.println("Failed to delete accounts");
            return false;
        }
    }
    public boolean taskDeleteAccount(int accountNr){

        // activates a method in correct account based on the account number. It will then be deleted
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNr() == accountNr){
                accounts.get(i).deleteMe();
                accounts.remove(i);
                return true;
            }
        }
        return false;
    }

    public void updateAll(){
            String query = "UPDATE users_swosh SET name=?, lastName=?, socialSecurityNr=?, password=? WHERE id=?";
            // Saves changes made to model in database
            try{
                Connection conn = GetConnection();
                PreparedStatement statement = conn.prepareStatement(query);

                String newHashPass = Encryptor.EncryptPassword(password)[0];

                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setInt(3, socialSecurityNr);
                statement.setString(4, newHashPass);
                statement.setInt(5, id);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new SQLException("Failed to update user");
                }
                conn.close();
            }catch(SQLException e){
                System.out.println("Failed to update user");
        }
    }
    public void updateOne(String select){
        //TODO
    }

    public void createAccount(int value, int accountNr) throws SQLException {
        Connection conn = GetConnection();
        String query = "INSERT INTO account_swosh( user_id ,value, accountNr ,created )\n" +
                "VALUE(?,?,?,?)";
        String time = String.valueOf(java.time.LocalDate.now());
        try {
            PreparedStatement statement = conn.prepareStatement(query, RETURN_GENERATED_KEYS);

            statement.setInt(1,id);
            statement.setInt(2,value);
            statement.setInt(3,accountNr);
            statement.setString(4,time);

            int rowsAffected = statement.executeUpdate();
            ResultSet getMyDamnKey = statement.getGeneratedKeys();

            if (rowsAffected > 0) {

                getMyDamnKey.next();
                int myDamnKey = getMyDamnKey.getInt(1);

                accounts.add(new Account(myDamnKey, id, value, accountNr, time));
                Gui.setAnnouncement("Account created");
                System.out.println("account Created!");
                UserBoard.updateUserBoard();
                conn.close();
            }else {
                conn.close();
                Gui.setAnnouncement("Account failed to be created");
                System.out.println("Account failed to be created ");
            }

        }catch (SQLException e){
            conn.close();
            Gui.setAnnouncement("Account failed to be created");
            System.out.println("Account failed to be created... ");
        }
    }

    public void getAllMyAccountsFromDatabase() throws SQLException {
        accounts.clear(); // Removes previous accounts
        Connection conn = GetConnection();
        String query = ("SELECT * FROM account_swosh WHERE user_id =? ORDER BY created DESC;");
            // Gets new accounts
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int accountId = resultSet.getInt("id");
                int value = resultSet.getInt("value");
                int accountNr = resultSet.getInt("accountNr");
                String time = resultSet.getString("created");

                accounts.add(new Account(accountId, id, value, accountNr, time)); // Saves accounts models in list
                if (Brain.currentUser != null){
                    UserBoard.updateUserBoard();
                }
            }
            System.out.println("successfully added all accounts");
        }catch (SQLException e){
            System.out.println("Couldn´t get all accounts of this user");
        }
        conn.close();
    }

    public boolean taskSendTransaction(int accountNr, int receiverAccountNr, int value) throws SQLException {
        Connection conn = GetConnection();
        String query = ("SELECT user_id, value FROM account_swosh WHERE accountNr = ?");
        int receiverId;
        int receiverCurrentValue;

        //Gets id and value from receiving account to be updated with new value
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1,receiverAccountNr);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                receiverId = resultSet.getInt("user_id");
                receiverCurrentValue = resultSet.getInt("value");
                conn.close();
            }else {
                conn.close();
                return false;
            }
        }catch (SQLException e){
            conn.close();
            System.out.println("could not get user id from that account number");
            return false;
        }

        // Activates method in corresponding account that will send the transaction, with the value to be updated
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).accountNr == accountNr){
                System.out.println(accounts.get(i).accountNr);
                accounts.get(i).sendNewTransaction(receiverId, receiverAccountNr, value, receiverCurrentValue);
                return true;
            }
        }
        System.out.println("Account nr is wrong, don´t forget to login");
        return false;
    }

    public boolean taskSearchTransactions(String lowDate, String highDate, int accountNrForSearch){


        // Activates method in corresponding account that will get the transactions based on dates
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).accountNr == accountNrForSearch){
                accounts.get(i).getTransactionsByDates(highDate, lowDate);
                return true;
            }
        }
        return false;
    }









    // Warning getters setters beyond this line
    //-------------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSocialSecurityNr() {
        return socialSecurityNr;
    }

    public void setSocialSecurityNr(int socialSecurityNr) {
        this.socialSecurityNr = socialSecurityNr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    public void getAllMySentTransactionsFromDatabase() throws SQLException {
//        sentTransactions.clear();
//        Connection conn = GetConnection();
//        String query = ("SELECT * FROM transaction_swosh WHERE user_id =? ORDER BY created DESC;");
//
//        try {
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setInt(1,id);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()){
//                int transactionId = resultSet.getInt("id");
//                int receiverId = resultSet.getInt("receiver_id");
//                int accountId = resultSet.getInt("account_ID");
//                int receiverAccountNr = resultSet.getInt("receiver_account_Nr");
//
//                int value = resultSet.getInt("value");
//                String time = resultSet.getString("created");
//                //TODO Remove this shit
//                //sentTransactions.add(new Transaction(transactionId, id, receiverId, accountId, receiverAccountNr, value, time));
//                if (Brain.currentUser != null){
//                    UserBoard.updateUserBoard();
//                }
//            }
//            System.out.println("successfully added all accounts");
//        }catch (SQLException e){
//            System.out.println("Couldn´t get all accounts of this user");
//        }
//        conn.close();
//    }

}
