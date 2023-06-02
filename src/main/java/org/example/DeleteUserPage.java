package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteUserPage {

    JPanel container;
    JPanel panel = new JPanel();
    public boolean isAdded = false;

    JLabel labelDelete = new JLabel("Sure you want to delete user?");
    JButton submitButton = new JButton("Submit");

    DeleteUserPage(JPanel container){
        this.container = container;
        drawMe();
    }
    void drawMe(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(200,25,650,550);

        labelDelete.setAlignmentX(labelDelete.CENTER_ALIGNMENT);

        submitButton.setAlignmentX(submitButton.CENTER_ALIGNMENT);
        submitButton.addActionListener(submitForm);

        panel.add(labelDelete);
        panel.add(submitButton);
    }

    ActionListener submitForm = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Brain.currentUser != null){

                    Brain.taskDeleteUser(); // Removes user

                    removeMe();
                    Gui.rePaint();

                    UserBoard.updateUserBoard();

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

}
