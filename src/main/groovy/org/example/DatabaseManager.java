package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/yourdbname";
    private static final String USER = "youruser";
    private static final String PASSWORD = "yourpassword";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createUser(String username, String role, String email) {
        String sql = "INSERT INTO users (username, role, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public void createTask(String title, String description, String status, String priority, String author, String assignee) {
        String sql = "INSERT INTO tasks (title, description, status, priority, author, assignee) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setString(3, status);
            pstmt.setString(4, priority);
            pstmt.setString(5, author);
            pstmt.setString(6, assignee);
            pstmt.executeUpdate();
            System.out.println("Task created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
    }
}
