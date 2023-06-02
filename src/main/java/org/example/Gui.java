package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class Gui {
    static JFrame frame = new JFrame("Swosh");
    private JPanel groupContainer = new JPanel();
    private UserBoard userBoard = new UserBoard(groupContainer);
    private CreateUserPage createUserPage = new CreateUserPage(groupContainer);
    private LoginPage login = new LoginPage(groupContainer);
    private AccountCreationPage accountCreationPage = new AccountCreationPage(groupContainer);
    private SendTransactionPage sendTransactionPage = new SendTransactionPage(groupContainer);
    private UpdateUserPage updateUserPage = new UpdateUserPage(groupContainer);

    private DeleteAccountPage deleteAccountPage = new DeleteAccountPage(groupContainer);
    private SearchTransactionsPage searchTransactionsPage = new SearchTransactionsPage(groupContainer);
    private DeleteUserPage deleteUserPage = new DeleteUserPage(groupContainer);
    private JButton createUserButton = new JButton("Create user");
    private JButton loginButton = new JButton("Login");
    private JButton accountCreationButton = new JButton("Create account");
    private JButton sendTransactionButton = new JButton("Send transaction");
    private JButton updateUserPageButton = new JButton("Update user");
    private JButton searchTransactionsButton = new JButton("Search transactions");
    private JButton deleteAccountButton = new JButton("Delete account");
    private JButton deleteUserButton = new JButton("Delete user");

    private static JTextArea publicAnnouncement = new JTextArea("Welcome");

    public Gui() throws SQLException, IOException {
        createAndDisplay();
    }

    public static void setAnnouncement(String announcement){
        publicAnnouncement.setText(announcement);
    }

    void createAndDisplay() throws IOException {

        groupContainer.setLayout(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        createUserButton.setBounds(20, 20, 150, 30);
        createUserButton.addActionListener(displayCreateUserPage);
        frame.add(createUserButton);
        loginButton.setBounds(20, 70, 150, 30);
        loginButton.addActionListener(displayLoginPage);
        frame.add(loginButton);
        accountCreationButton.setBounds(20,120,150,30);
        accountCreationButton.addActionListener(displayAccountCreationPage);
        frame.add(accountCreationButton);
        sendTransactionButton.setBounds(20,170,150,30);
        sendTransactionButton.addActionListener(displaySendCreationPage);
        frame.add(sendTransactionButton);
        updateUserPageButton.setBounds(20,220,150,30);
        updateUserPageButton.addActionListener(displayUpdatePage);
        frame.add(updateUserPageButton);
        searchTransactionsButton.setBounds(20,270,150,30);
        searchTransactionsButton.addActionListener(displaySearchTransactionsPage);
        frame.add(searchTransactionsButton);
        deleteAccountButton.setBounds(20,320,150,30);
        deleteAccountButton.addActionListener(displayDeletePage);
        frame.add(deleteAccountButton);
        deleteUserButton.setBounds(20,370,150,30);
        deleteUserButton.addActionListener(displayDeleteUserPage);
        frame.add(deleteUserButton);
        publicAnnouncement.setBounds(20,420,150,90);
        frame.add(publicAnnouncement);
        publicAnnouncement.setLineWrap(true);
        publicAnnouncement.setEditable(false);


        frame.add(groupContainer);

        frame.setVisible(true);
        frame.repaint();
        frame.revalidate();

    }

    // displays and hides the different pages
    ActionListener displayCreateUserPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (createUserPage.isAdded == false){
                removeCurrentPage();
                createUserPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displayAccountCreationPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (accountCreationPage.isAdded == false){
                removeCurrentPage();
                accountCreationPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displayLoginPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            setAnnouncement("Ive used same password as username, for your \nconvenience.");

            if (login.isAdded == false){
                removeCurrentPage();
                login.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displaySendCreationPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (sendTransactionPage.isAdded == false){
                removeCurrentPage();
                sendTransactionPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displayUpdatePage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (updateUserPage.isAdded == false){
                removeCurrentPage();
                updateUserPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displaySearchTransactionsPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (searchTransactionsPage.isAdded == false){
                removeCurrentPage();
                searchTransactionsPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displayDeletePage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (deleteAccountPage.isAdded == false){
                removeCurrentPage();
                deleteAccountPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };
    ActionListener displayDeleteUserPage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (deleteUserPage.isAdded == false){
                removeCurrentPage();
                deleteUserPage.addMe();
            }else {
                removeCurrentPage();
            }
            rePaint();

        }
    };

    // Hides all pages
    void removeCurrentPage(){
        login.removeMe();
        createUserPage.removeMe();
        accountCreationPage.removeMe();
        sendTransactionPage.removeMe();
        updateUserPage.removeMe();
        searchTransactionsPage.removeMe();
        deleteAccountPage.removeMe();
        deleteUserPage.removeMe();
    }

    // Repaints the GUI and updates the view
    static void rePaint(){
        frame.repaint();
        frame.revalidate();
    }
}
