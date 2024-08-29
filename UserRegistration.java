package foodmanagementsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTextField;


public class UserRegistration {

    private JFrame frame;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField addressField;

    public UserRegistration() {
        // Create frame
        frame = new JFrame("User Registration");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create and add components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 20, 80, 25);
        frame.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 165, 25);
        frame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        frame.add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setBounds(100, 50, 165, 25);
        frame.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 80, 80, 25);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 80, 165, 25);
        frame.add(emailField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setBounds(10, 110, 100, 25);
        frame.add(phoneNumberLabel);

        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(120, 110, 145, 25);
        frame.add(phoneNumberField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(10, 140, 80, 25);
        frame.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(100, 140, 165, 25);
        frame.add(addressField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10, 170, 150, 25);
        frame.add(registerButton);

        // Add action listener
        registerButton.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
                handleRegisterUser();
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }

    private void handleRegisterUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();

        registerUser(username, password, email, phoneNumber, address);
    }

    public static void registerUser(String username, String password, String email, String phoneNumber, String address) {
        // Check if username already exists
        if (userExists(username)) {
            JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.");
            return;
        }

        String query = "INSERT INTO Users (user_id, username, password, email, phone_number, address) VALUES (user_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameters
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, address);

            // Execute update
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "User registered successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        // Launch GUI
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                new UserRegistration();
            }
        });
    }
}
