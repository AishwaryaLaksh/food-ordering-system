package foodmanagementsystem;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderFood {

    public static void displayFoodMenu() {
        String query = "SELECT item_id, item_name, price FROM FoodItems";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder menu = new StringBuilder("Food Menu:\n");
            while (rs.next()) {
                menu.append(String.format("ID: %d, Name: %s, Price: %.2f%n",
                        rs.getInt("item_id"), rs.getString("item_name"), rs.getDouble("price")));
            }
            JOptionPane.showMessageDialog(null, menu.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error displaying food menu: " + e.getMessage());
        }
    }

    public static void placeOrder(String username, int[] itemIds, int[] quantities) {
        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            JOptionPane.showMessageDialog(null, "User not found.");
            return;
        }

        if (itemIds.length != quantities.length) {
            JOptionPane.showMessageDialog(null, "Mismatched item IDs and quantities.");
            return;
        }

        String orderQuery = "INSERT INTO Orders (order_id, user_id, total_amount) VALUES (order_seq.NEXTVAL, ?, ?)";
        String orderItemQuery = "INSERT INTO OrderItems (order_item_id, order_id, item_id, quantity) VALUES (order_item_seq.NEXTVAL, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement orderStmt = con.prepareStatement(orderQuery, new String[]{"order_id"});
             PreparedStatement orderItemStmt = con.prepareStatement(orderItemQuery)) {

            // Calculate total amount
            double totalAmount = calculateTotalAmount(con, itemIds, quantities);
            
            // Insert order details
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, totalAmount);
            orderStmt.executeUpdate();
            
            // Retrieve generated order ID
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                // Insert order items
                for (int i = 0; i < itemIds.length; i++) {
                    orderItemStmt.setInt(1, orderId);
                    orderItemStmt.setInt(2, itemIds[i]);
                    orderItemStmt.setInt(3, quantities[i]);
                    orderItemStmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Order placed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to create order.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error placing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double calculateTotalAmount(Connection con, int[] itemIds, int[] quantities) throws SQLException {
        double totalAmount = 0;

        String priceQuery = "SELECT price FROM FoodItems WHERE item_id = ?";
        try (PreparedStatement priceStmt = con.prepareStatement(priceQuery)) {
            for (int i = 0; i < itemIds.length; i++) {
                priceStmt.setInt(1, itemIds[i]);
                try (ResultSet rs = priceStmt.executeQuery()) {
                    if (rs.next()) {
                        double price = rs.getDouble("price");
                        totalAmount += price * quantities[i];
                    } else {
                        JOptionPane.showMessageDialog(null, "Item ID " + itemIds[i] + " not found.");
                    }
                }
            }
        }

        return totalAmount;
    }

    private static int getUserIdByUsername(String username) {
        String query = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving user ID: " + e.getMessage());
        }
        return -1;
    }
}
