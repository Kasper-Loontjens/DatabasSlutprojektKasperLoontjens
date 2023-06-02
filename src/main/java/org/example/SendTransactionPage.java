package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SendTransactionPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelAccNr = new JLabel("Send from account Number:");
    JTextField accNrField = new JTextField();
    JLabel labelReceiverNr = new JLabel("To account Number:");
    JTextField receiverNrField = new JTextField();
    JLabel labelValue = new JLabel("Value:");
    JTextField valueField = new JTextField();

    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    SendTransactionPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelAccNr.setAlignmentX(labelAccNr.CENTER_ALIGNMENT);
        accNrField.setMaximumSize(barSize);

        labelReceiverNr.setAlignmentX(labelReceiverNr.CENTER_ALIGNMENT);
        receiverNrField.setMaximumSize(barSize);

        labelValue.setAlignmentX(labelValue.CENTER_ALIGNMENT);
        valueField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);


        panel.add(labelAccNr);
        panel.add(accNrField);
        panel.add(labelReceiverNr);
        panel.add(receiverNrField);
        panel.add(labelValue);
        panel.add(valueField);
        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Brain.currentUser != null){
                if (isInteger(accNrField.getText()) &&
                        isInteger(receiverNrField.getText()) &&
                        isInteger(valueField.getText())) // Makes sure user is logged in and have filled out form
                {

                    int accountNr = Integer.parseInt(accNrField.getText());
                    int receiverAccountNr = Integer.parseInt(receiverNrField.getText());
                    int value =  Integer.parseInt(valueField.getText());

                    try {
                        Brain.currentUser.taskSendTransaction(accountNr,receiverAccountNr,value); // Sends new transaction

                        accNrField.setText("");
                        receiverNrField.setText("");
                        valueField.setText("");

                        removeMe();
                        Gui.rePaint();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }else {
                    Gui.setAnnouncement("Fill out the form please");
                    System.out.println("You have to write numbers in field");
                }
            }else {
                Gui.setAnnouncement("Logg in please");
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
