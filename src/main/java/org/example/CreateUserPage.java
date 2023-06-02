package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreateUserPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelName = new JLabel("Name:");
    JTextField nameField = new JTextField();
    JLabel labelLastName = new JLabel("Last Name:");
    JTextField lastNameField = new JTextField();
    JLabel labelSocialNr = new JLabel("Social Security Number:");
    JTextField SocialNrField = new JTextField();
    JLabel labelPassword = new JLabel("Password:");
    JTextField passwordField = new JTextField();

    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);

    CreateUserPage(JPanel container){
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
        SocialNrField.setMaximumSize(barSize);

        labelPassword.setAlignmentX(labelPassword.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);


        panel.add(labelName);
        panel.add(nameField);
        panel.add(labelLastName);
        panel.add(lastNameField);
        panel.add(labelSocialNr);
        panel.add(SocialNrField);
        panel.add(labelPassword);
        panel.add(passwordField);
        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!nameField.getText().isBlank() &&
                    !lastNameField.getText().isBlank() &&
                    isInteger(SocialNrField.getText()) &&
                    !passwordField.getText().isBlank() // Makes sure all fields are filled
            ) {
                System.out.println("Success!!!!");
                String userName = nameField.getText();
                String lastName = lastNameField.getText();
                int socialSecurityNr =  Integer.parseInt(SocialNrField.getText());
                String password = passwordField.getText();


                try {
                    Brain.createUser(userName,lastName,socialSecurityNr,password); // Creates user
                    nameField.setText("");
                    lastNameField.setText("");
                    SocialNrField.setText("");
                    passwordField.setText("");

                    removeMe();
                    Gui.rePaint();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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
