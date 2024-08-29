package foodmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("User Login");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(3, 2));

            frame.add(new JLabel("Username:"));
            JTextField usernameField = new JTextField();
            frame.add(usernameField);

            frame.add(new JLabel("Password:"));
            JPasswordField passwordField = new JPasswordField();
            frame.add(passwordField);

            JButton loginButton = new JButton("Login");
            frame.add(loginButton);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    boolean success = loginUser(username, password);
                    if (success) {
                        openOrderMenu();
                    }
                }
            });

            frame.setVisible(true);
        });
    }

    public static boolean loginUser(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error during login: " + e.getMessage());
            return false;
        }
    }

    private static void openOrderMenu() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Food");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JButton viewMenuButton = new JButton("View Food Menu");
            frame.add(viewMenuButton, BorderLayout.NORTH);

            viewMenuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OrderFood.displayFoodMenu();
                }
            });

            JButton placeOrderButton = new JButton("Place Order");
            frame.add(placeOrderButton, BorderLayout.SOUTH);

            placeOrderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openOrderForm();
                }
            });

            frame.setVisible(true);
        });
    }

    private static void openOrderForm() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Place Order");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new GridLayout(4, 2));

            frame.add(new JLabel("Item IDs (comma-separated):"));
            JTextField itemIdsField = new JTextField();
            frame.add(itemIdsField);

            frame.add(new JLabel("Quantities (comma-separated):"));
            JTextField quantitiesField = new JTextField();
            frame.add(quantitiesField);

            JButton orderButton = new JButton("Place Order");
            frame.add(orderButton);

            orderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String itemIdsText = itemIdsField.getText();
                    String quantitiesText = quantitiesField.getText();
                    
                    int[] itemIds = parseCommaSeparatedIntegers(itemIdsText);
                    int[] quantities = parseCommaSeparatedIntegers(quantitiesText);

                    if (itemIds.length == 0 || quantities.length == 0) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please check your item IDs and quantities.");
                        return;
                    }

                    String username = JOptionPane.showInputDialog("Enter your username");
                    OrderFood.placeOrder(username, itemIds, quantities);
                }
            });

            frame.setVisible(true);
        });
    }

    private static int[] parseCommaSeparatedIntegers(String text) {
        String[] parts = text.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + parts[i] + " is not a number.");
                return new int[0]; // Return an empty array to indicate an error
            }
        }
        return result;
    }
}
