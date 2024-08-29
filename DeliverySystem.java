package foodmanagementsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliverySystem {

    private JFrame frame;
    private JTextField orderIdField;
    private JTextField deliveryAddressField;

    public DeliverySystem() {
        // Create frame
        frame = new JFrame("Delivery System");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create and add components
        JLabel orderIdLabel = new JLabel("Order ID:");
        orderIdLabel.setBounds(10, 20, 80, 25);
        frame.add(orderIdLabel);

        orderIdField = new JTextField();
        orderIdField.setBounds(100, 20, 165, 25);
        frame.add(orderIdField);

        JLabel addressLabel = new JLabel("Delivery Address:");
        addressLabel.setBounds(10, 50, 120, 25);
        frame.add(addressLabel);

        deliveryAddressField = new JTextField();
        deliveryAddressField.setBounds(130, 50, 165, 25);
        frame.add(deliveryAddressField);

        JButton deliverButton = new JButton("Deliver");
        deliverButton.setBounds(10, 80, 150, 25);
        frame.add(deliverButton);

        // Add action listener
        deliverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeliverOrder();
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }

    private void handleDeliverOrder() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText());
            String deliveryAddress = deliveryAddressField.getText();

            deliverOrder(orderId, deliveryAddress);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Order ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void deliverOrder(int orderId, String deliveryAddress) {
        String checkOrderQuery = "SELECT COUNT(*) FROM Orders WHERE order_id = ?";
        String deliveryQuery = "INSERT INTO Delivery (delivery_id, order_id, delivery_address) VALUES (delivery_seq.NEXTVAL, ?, ?)";

        try (Connection con = Database.getConnection()) {

            // Check if the Order ID exists
            try (PreparedStatement checkOrderStmt = con.prepareStatement(checkOrderQuery)) {
                checkOrderStmt.setInt(1, orderId);
                ResultSet rs = checkOrderStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null, "Order ID does not exist. Please enter a valid Order ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insert delivery details
            try (PreparedStatement deliveryStmt = con.prepareStatement(deliveryQuery)) {
                deliveryStmt.setInt(1, orderId);
                deliveryStmt.setString(2, deliveryAddress);
                deliveryStmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Order delivered successfully!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error during delivery: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Launch GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliverySystem();
            }
        });
    }
}
