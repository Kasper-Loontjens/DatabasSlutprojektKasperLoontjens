package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class Account extends Model{
    protected int id;
    protected int userId;
    protected int value;
    protected int accountNr;
    protected String created;
    public Account(int id, int userId, int value, int accountNr, String created) {
        this.id = id;
        this.userId = userId;
        this.value = value;
        this.accountNr = accountNr;
        this.created = created;
    }

    public void deleteMe(){

        //Deletes this account from database

        String query =  "DELETE FROM account_swosh WHERE id = ?;";
        try {
            Connection conn = GetConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1,id);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                Gui.setAnnouncement("Failed to delete account");
                throw new SQLException("Failed to delete account");
            }else {
                Gui.setAnnouncement("Account Deleted");
            }

            conn.close();

        } catch (SQLException e) {
            Gui.setAnnouncement("Failed to delete account");
            throw new RuntimeException(e);
        }

    }

    public void sendNewTransaction(int receiverId,int receiverAccountNr, int transValue, int receiverAccountValue) throws SQLException {
        Connection conn = GetConnection();
        String query = "INSERT INTO transaction_swosh( user_id ,receiver_id, account_id, receiver_account_Nr, value ,created )\n" +
                "VALUE(?,?,?,?,?,?)";
        String time = String.valueOf(java.time.LocalDate.now());

        if ((value - transValue) >= 0){ //   if account has enough money
            try {
                PreparedStatement statement = conn.prepareStatement(query, RETURN_GENERATED_KEYS);

                statement.setInt(1,userId);
                statement.setInt(2,receiverId);
                statement.setInt(3,id);
                statement.setInt(4,receiverAccountNr);
                statement.setInt(5, transValue);
                statement.setString(6,time);

                int rowsAffected = statement.executeUpdate();
                ResultSet getMyDamnKey = statement.getGeneratedKeys();

                if (rowsAffected > 0) {

                    //Remnants of old code Saved for my own purpose
                    //getMyDamnKey.next();
                    //int myDamnKey = getMyDamnKey.getInt(1);

                    Gui.setAnnouncement("Transaction successful");
                    System.out.println("Transaction Created!");
                    value = (value-transValue); // saves new value of this account

                    //Updates the new values to accounts in database
                    updateValuesInAccounts((receiverAccountValue + transValue), receiverAccountNr);

                    //Remnants of old code. Saved for my own purpose
                    //Brain.currentUser.sentTransactions.add(new Transaction(myDamnKey,userId,receiverId,id,receiverAccountNr,transValue,time, accountNr));
                    conn.close();
                    UserBoard.updateUserBoard();
                }else {
                    Gui.setAnnouncement("Transaction failed to be created");
                    System.out.println("Transaction failed to be created ");
                }

            }catch (SQLException e){
                Gui.setAnnouncement("Transaction failed");
                System.out.println("Transaction failed");
            }
        }else {
            Gui.setAnnouncement("Not enough money");
            System.out.println("Not enough money");
        }
        conn.close();
    }

    public void updateValuesInAccounts(int receiverNewValue, int receiverAccountNr ){
        String query = "UPDATE account_swosh SET value = ? WHERE accountNr = ?; ";
        String query2 = "UPDATE account_swosh SET value = ? WHERE id = ?;";

        try{
            Connection conn = GetConnection();

            PreparedStatement statement = conn.prepareStatement(query);
            PreparedStatement statement2 = conn.prepareStatement(query2);

            statement.setInt(1, receiverNewValue);
            statement.setInt(2, receiverAccountNr);
            statement2.setInt(1, value);
            statement2.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to change Values");
            }
            int rowsUpdated2 = statement2.executeUpdate();
            if (rowsUpdated2 == 0) {
                throw new SQLException("Failed to change Values");
            }

            //If receiving account is owned by the current user it is updated
            for (int i = 0; i < Brain.currentUser.accounts.size(); i++) {
                    if (Brain.currentUser.accounts.get(i).accountNr == receiverAccountNr){
                        Brain.currentUser.accounts.get(i).value = receiverNewValue;
                    }
            }
            conn.close();
        }catch(SQLException e){
            Gui.setAnnouncement("Failed to update values in database");
            System.out.println("Connection problems when updating account values");

        }
    }
    public void getTransactionsByDates(String highDate, String lowDate){
        //Old
        //String querySubstituted = "SELECT * FROM transaction_Swosh WHERE created >= ? AND created <= ? AND account_id = ? OR receiver_account_Nr = ? ORDER BY created DESC ;";

        //Gets info from account and transaction in database, ordered by date newest first.
        String query = "SELECT transaction_swosh.id AS transaction_Id,\n" +
                "transaction_swosh.user_id AS sender_user_id,\n" +
                "receiver_id,\n" +
                "account_id,\n" +
                "receiver_account_Nr,\n" +
                "transaction_swosh.value AS transaction_value,\n" +
                "transaction_swosh.created AS transaction_created,\n" +
                "account_swosh.accountNR AS sender_account_nr\n" +
                "FROM transaction_swosh LEFT JOIN account_swosh ON transaction_swosh.account_id =\n" +
                "account_swosh.id WHERE transaction_swosh.created >= ? AND transaction_swosh.created <= ? AND account_id = ? OR receiver_account_Nr = ? ORDER BY transaction_swosh.created DESC ;";
        try {
            Connection conn = GetConnection();

            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, lowDate);
            statement.setString(2, highDate);
            statement.setInt(3, id);
            statement.setInt(4, accountNr);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int transId = resultSet.getInt("transaction_Id");
                int transUserId = resultSet.getInt("sender_user_id");
                int transReceiverId = resultSet.getInt("receiver_id");
                int transAccountId = resultSet.getInt("account_id");
                int transReceiverAccountNr = resultSet.getInt("receiver_account_Nr");
                int transValue = resultSet.getInt("transaction_value");
                String time = resultSet.getString("transaction_created");
                int senderAccountNr = resultSet.getInt("sender_account_nr");

                //Saves the model of the transaction in a list owned by the model of the user
                Brain.currentUser.searchedTransactions.add(new Transaction(transId,transUserId,transReceiverId,transAccountId,transReceiverAccountNr,transValue,time, senderAccountNr));

            }
            conn.close();

        } catch (SQLException e) {
            Gui.setAnnouncement("Failed to get transactions from database");
            throw new RuntimeException(e);
        }
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(int accountNr) {
        this.accountNr = accountNr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


}
