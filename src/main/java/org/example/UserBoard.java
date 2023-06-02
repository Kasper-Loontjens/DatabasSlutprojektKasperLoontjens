package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UserBoard {

    JPanel panel = new JPanel();
    JPanel container;

    JLabel jLabel = new JLabel("Users");

    static JTextArea jText = new JTextArea("");

    UserBoard(JPanel container) throws SQLException {
        this.container = container;
        drawMe();
    }
    void drawMe() throws SQLException {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(900,0,300,600);
        panel.add(jLabel);
        jLabel.setAlignmentX(jLabel.CENTER_ALIGNMENT);

        jText.setMaximumSize(new Dimension(300,500));
        jText.setEditable(false);
        //jText.setBackground(Color.LIGHT_GRAY);
        jText.setOpaque(false);
        jText.setText("Not logged in");
        panel.add(jText);
        container.add(panel);

    }
    public static void updateUserBoard() {

        // Gets the information from the models to be displayed.
        if (Brain.currentUser != null){
            String toWriteAccountsInfo = "";
            String toWriteTransactionsSentInfo = "";
            String toWriteSearchedTransactions = "";
            if (!Brain.currentUser.accounts.isEmpty()){
                toWriteAccountsInfo = "Accounts: \n";
                for (int i = 0; i < Brain.currentUser.accounts.size(); i++) {
                    toWriteAccountsInfo += ("Account Nr: "+Brain.currentUser.accounts.get(i).getAccountNr() +
                            " | "+ Brain.currentUser.accounts.get(i).getValue() + " kr "+
                            Brain.currentUser.accounts.get(i).getCreated()+"\n");
                }
            }
//            if (!Brain.currentUser.sentTransactions.isEmpty()){
//                toWriteTransactionsSentInfo = "\nSent Transactions: \n";
//                for (int i = 0; i < Brain.currentUser.sentTransactions.size(); i++) {
//                    toWriteTransactionsSentInfo += ("To receiving nr: "+Brain.currentUser.sentTransactions.get(i).getReceiver_account_Nr() + "\n");
//                }
//            }
            if (!Brain.currentUser.searchedTransactions.isEmpty()){
                toWriteSearchedTransactions = "\nSearched transactions: \n";
                for (int i = 0; i < Brain.currentUser.searchedTransactions.size(); i++) {
                    toWriteSearchedTransactions += ("From AccountNr: " +Brain.currentUser.searchedTransactions.get(i).getAccount_nr()+
                            ". to: " + Brain.currentUser.searchedTransactions.get(i).getReceiver_account_Nr() +
                            " | "+ Brain.currentUser.searchedTransactions.get(i).getValue() + "Kr | "+
                            Brain.currentUser.searchedTransactions.get(i).getCreated() + " \n");
                }
            }
            jText.setText(
                    Brain.currentUser.getName()+ " "+ Brain.currentUser.getLastName() + ". SocialNr: " +
                            Brain.currentUser.getSocialSecurityNr()
                            +"\n"+
                            toWriteAccountsInfo + toWriteSearchedTransactions
            );
        }
        else {
            jText.setText("Not logged in");
        }

        
    }
}
