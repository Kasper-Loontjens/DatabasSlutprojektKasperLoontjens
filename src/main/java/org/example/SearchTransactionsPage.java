package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SearchTransactionsPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelAccNr = new JLabel("Account Number:");
    JTextField accNrField = new JTextField();
    JLabel labelEarlierDate = new JLabel("From earlier date:");
    JTextField earlierDateField = new JTextField();
    JLabel labelLaterDate = new JLabel("To later date:");
    JTextField laterDateField = new JTextField();
    JLabel labelTip = new JLabel("Ex 2023-06-01:");

    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    SearchTransactionsPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelAccNr.setAlignmentX(labelAccNr.CENTER_ALIGNMENT);
        accNrField.setMaximumSize(barSize);

        labelEarlierDate.setAlignmentX(labelEarlierDate.CENTER_ALIGNMENT);
        earlierDateField.setMaximumSize(barSize);

        labelLaterDate.setAlignmentX(labelLaterDate.CENTER_ALIGNMENT);
        laterDateField.setMaximumSize(barSize);

        labelTip.setAlignmentX(labelTip.CENTER_ALIGNMENT);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);


        panel.add(labelAccNr);
        panel.add(accNrField);
        panel.add(labelEarlierDate);
        panel.add(earlierDateField);
        panel.add(labelLaterDate);
        panel.add(laterDateField);
        panel.add(labelTip);
        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Brain.currentUser != null){
                if (isInteger(accNrField.getText()) &&
                        !earlierDateField.getText().isBlank() &&
                        !laterDateField.getText().isBlank() // makes sure user is logged in and fields are filled
                ) {
                    int accNr = Integer.parseInt(accNrField.getText());
                    String lowDate = earlierDateField.getText();
                    String highDate =  laterDateField.getText();

                    if (Brain.currentUser.taskSearchTransactions(lowDate,highDate,accNr)){ //Searches for the transactions to display by date
                        System.out.println("Successfully found account");
                        accNrField.setText("");
                        earlierDateField.setText("");
                        laterDateField.setText("");

                        removeMe();
                        Gui.rePaint();
                        UserBoard.updateUserBoard();
                    }else {
                        System.out.println("Failed to find account");
                    }
            }else {
                    Gui.setAnnouncement("Not logged in");
                }

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
