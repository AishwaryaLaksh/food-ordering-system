package foodmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodManagementApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Food Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // Add a simple welcome label
            JLabel welcomeLabel = new JLabel("Welcome to the Food Management System", SwingConstants.CENTER);
            frame.add(welcomeLabel, BorderLayout.CENTER);

            // Create a button to simulate user login
            JButton loginButton = new JButton("Login");
            frame.add(loginButton, BorderLayout.SOUTH);

            // Add action listener for the button
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Open login window or perform login action
                    JOptionPane.showMessageDialog(frame, "Login button clicked!");
                }
            });

            frame.setVisible(true);
        });
    }
}
