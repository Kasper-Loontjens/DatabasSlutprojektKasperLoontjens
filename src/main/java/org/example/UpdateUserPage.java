package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UpdateUserPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelName = new JLabel("New name:");
    JTextField nameField = new JTextField();
    JLabel labelLastName = new JLabel(" New last name:");
    JTextField lastNameField = new JTextField();
    JLabel labelSocialNr = new JLabel("New Social Security Number:");
    JTextField socialNrField = new JTextField();
    JLabel labelPassword = new JLabel("New Password:");
    JTextField passwordField = new JTextField();

    JLabel labelTip = new JLabel("Tip: only updates fields that are filled");


    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    UpdateUserPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelName.setAlignmentX(labelName.CENTER_ALIGNMENT);
        nameField.setMaximumSize(barSize);

        labelLastName.setAlignmentX(labelLastName.CENTER_ALIGNMENT);
        lastNameField.setMaximumSize(barSize);

        labelSocialNr.setAlignmentX(labelSocialNr.CENTER_ALIGNMENT);
        socialNrField.setMaximumSize(barSize);

        labelPassword.setAlignmentX(labelPassword.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);

        labelTip.setAlignmentX(labelTip.CENTER_ALIGNMENT);


        panel.add(labelName);
        panel.add(nameField);
        panel.add(labelLastName);
        panel.add(lastNameField);
        panel.add(labelSocialNr);
        panel.add(socialNrField);
        panel.add(labelPassword);
        panel.add(passwordField);
        panel.add(submitButton);
        panel.add(labelTip);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Brain.currentUser != null){ // makes sure user is logged in

                // Updates the user based on which fields has been filled
                if (!nameField.getText().isBlank()){
                    Brain.currentUser.setName(nameField.getText());
                }
                if (!lastNameField.getText().isBlank()){
                    Brain.currentUser.setLastName(lastNameField.getText());
                }
                if (isInteger(socialNrField.getText())){
                    Brain.currentUser.setSocialSecurityNr(Integer.parseInt(socialNrField.getText()));
                }
                if (!passwordField.getText().isBlank()) {
                    Brain.currentUser.setPassword(passwordField.getText());
                }


                Brain.currentUser.updateAll(); // updates the new information


                nameField.setText("");
                lastNameField.setText("");
                socialNrField.setText("");
                passwordField.setText("");

                removeMe();
                Gui.rePaint();

                UserBoard.updateUserBoard();

            }else {
                Gui.setAnnouncement("Logg in please");
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
