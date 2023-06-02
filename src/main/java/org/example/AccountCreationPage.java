package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AccountCreationPage {


    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelAmount = new JLabel("Amount:");
    JTextField amountField = new JTextField();
    JLabel labelAccountNr = new JLabel("Account Number:");
    JTextField accountNrField = new JTextField();

    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    AccountCreationPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelAmount.setAlignmentX(labelAmount.CENTER_ALIGNMENT);
        amountField.setMaximumSize(barSize);

        labelAccountNr.setAlignmentX(labelAccountNr.CENTER_ALIGNMENT);
        accountNrField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);


        panel.add(labelAmount);
        panel.add(amountField);
        panel.add(labelAccountNr);
        panel.add(accountNrField);
        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isInteger(amountField.getText()) &&
                isInteger(accountNrField.getText()) &&
                    Brain.currentUser != null // if fields are filled, and user logged in
            ) {
                int amount = Integer.parseInt(amountField.getText());
                int accountNr = Integer.parseInt(accountNrField.getText());

                try {
                    Brain.currentUser.createAccount(amount, accountNr); //Creates new account
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                amountField.setText("");
                accountNrField.setText("");

                removeMe();
                Gui.rePaint();
            }else {
                Gui.setAnnouncement("Logg in or write numbers in fields");
                System.out.println("Logg in or write numbers in fields");
            }
        }
    };

    void addMe(){
        isAdded = true;
        container.add(panel);
    }
    void removeMe(){
        isAdded = false;
        container.remove(panel);
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
