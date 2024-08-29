package foodmanagementsystem;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Ensure the SID/service name is correct
        String username = "aishwarya24"; // Correct username
        String password = "password1"; // Correct password, case-sensitive

        return DriverManager.getConnection(url, username, password);
    }

    // Method to register a user
    public static void registerUser(String username, String password, String email, String phoneNumber, String address) {
        String query = "INSERT INTO Users (user_id, username, password, email, phone_number, address) VALUES (user_seq.NEXTVAL, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection(); // Use Database class for connection
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameters
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, address);

            // Execute update
            stmt.executeUpdate();

            System.out.println("User registered successfully!");

        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method for testing user registration
    public static void main(String[] args) {
        // Example usage of registerUser method
        registerUser("john_doe", "password123", "john@example.com", "1234567890", "123 Main St");
    }
}
