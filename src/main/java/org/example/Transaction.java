package org.example;

public class Transaction {
    protected int id;
    protected int user_id;
    protected int receiver_id;
    protected int account_id;

    protected int account_nr;
    protected int receiver_account_Nr;
    protected int value;
    protected String created;

    public Transaction(int id, int user_id, int receiver_id, int account_id,int receiver_account_Nr, int value, String created, int account_nr) {
        this.id = id;
        this.user_id = user_id;
        this.receiver_id = receiver_id;
        this.account_id = account_id;
        this.receiver_account_Nr = receiver_account_Nr;
        this.value = value;
        this.created = created;
        this.account_nr = account_nr;
    }

//Not much to see used to save model for use in displaying information in userboard



    // Warning getters setters beyond this line
    //-------------------------------------------------------------------------------------------


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getAccount_nr() {
        return account_nr;
    }

    public void setAccount_nr(int account_nr) {
        this.account_nr = account_nr;
    }


    public int getReceiver_account_Nr() {
        return receiver_account_Nr;
    }

    public void setReceiver_account_Nr(int receiver_account_Nr) {
        this.receiver_account_Nr = receiver_account_Nr;
    }
}
