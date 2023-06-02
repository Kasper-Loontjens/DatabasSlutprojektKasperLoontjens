package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteAccountPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelAccNr = new JLabel("Account Number to delete:");
    JTextField accNrField = new JTextField();

    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    DeleteAccountPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelAccNr.setAlignmentX(labelAccNr.CENTER_ALIGNMENT);
        accNrField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);



        panel.add(labelAccNr);
        panel.add(accNrField);

        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Brain.currentUser != null){
                if (isInteger(accNrField.getText())){ // Checks field and makes sure user is logged in

                    int accNr = Integer.parseInt(accNrField.getText());

                    Brain.currentUser.taskDeleteAccount(accNr); // Deletes account written by user

                    accNrField.setText("");

                    removeMe();
                    Gui.rePaint();

                    UserBoard.updateUserBoard();

                }


            }else {
                Gui.setAnnouncement("Not logged in");
                System.out.println("Not logged in");
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
