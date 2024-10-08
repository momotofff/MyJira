package org.example;

import swagger.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager
{
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createUser(String username, String role, String email)
    {
        String sql = "INSERT INTO users (username, role, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.setString(3, email);

            // TODO: Here we have to get user ID generated by DB
            pstmt.executeUpdate();
            System.out.println("User created successfully!");
        }
        catch (SQLException e)
        {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public List<User> getUsers() throws SQLException
    {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next())
        {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setRole(User.RoleEnum.fromValue(rs.getString("role")));
            list.add(user);
        }

        return list;
    }

    public void createTask(String title, String description, String status, String priority, String author, String assignee)
    {
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
