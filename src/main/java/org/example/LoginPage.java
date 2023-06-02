package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPage {

    JPanel container;
    JPanel panel = new JPanel();

    public boolean isAdded = false;

    JLabel labelSocialNr = new JLabel("Social Security Number:");
    JTextField socialNrField = new JTextField();
    JLabel labelPassword = new JLabel("Password:");
    JTextField passwordField = new JTextField();
    JButton submitButton = new JButton("Submit");

    Dimension barSize = new Dimension(200,20);
    LoginPage(JPanel container){
        this.container = container;
        drawMe();
    }

    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelSocialNr.setAlignmentX(labelSocialNr.CENTER_ALIGNMENT);
        socialNrField.setMaximumSize(barSize);

        labelPassword.setAlignmentX(labelPassword.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(barSize);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);

        panel.add(labelSocialNr);
        panel.add(socialNrField);
        panel.add(labelPassword);
        panel.add(passwordField);
        panel.add(submitButton);

    }

    void addMe(){
        isAdded = true;
        container.add(panel);
    }
    void removeMe(){
        isAdded = false;
        container.remove(panel);
    }
    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!socialNrField.getText().isBlank() &&
                    !passwordField.getText().isBlank()
            ) {
                int socialNr = Integer.parseInt(socialNrField.getText());
                String password = passwordField.getText();
                try {
                    int id = Brain.getIdAndCheckPassword(socialNr,password); // Checks password

                    if (id > 0){
                        Brain.login(id); // Logs in as user
                        Gui.setAnnouncement("You´ve logged in");
                        System.out.println("You´ve logged in");
                    }
                } catch (SQLException ex) {
                    System.out.println("Failed to login");
                    throw new RuntimeException(ex);
                }


                socialNrField.setText("");
                passwordField.setText("");

                removeMe();
                Gui.rePaint();
            }else {
                Gui.setAnnouncement("Fields cannot be empty");
                System.out.println("Field cannot be empty");
            }
        }
    };

}
