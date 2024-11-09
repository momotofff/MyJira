package momotoff.myjira.dbmanager;

import io.swagger.model.User;
import io.swagger.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class UsersDbManager
{
    // TODO: Relocate all user-related methods here
    public static User createUser(Connection connection, String username, String role, String email) throws SQLException
    {
        String sql = "INSERT INTO users (username, role, email) VALUES (?, ?::userrole, ?)";
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);
            preparedStatement.setString(3, email);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        user = new User();
                        user.setId(id);
                        user.setUsername(username);
                        user.setRole(UserRole.fromValue(role));
                        user.setEmail(email);
                        System.out.printf("User with ID = %d created successfully!%n", id);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            if (e.getMessage().contains("Duplicate key value violates unique constraint"))
                throw new SQLException("Username already exists: " + username, e);


            throw new SQLException("Error creating user: " + username, e);
        }
        catch (IllegalArgumentException e)
        {
            throw new SQLException("Invalid role: " + role);
        }

        return user;
    }

    public static List<User> getUsers(Connection connection) throws SQLException
    {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            while (resultSet.next())
            {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(UserRole.fromValue(resultSet.getString("role")));
                list.add(user);
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error retrieving users", e);
        }

        return list;
    }

    public static User getUserByName(Connection connection, String name) throws SQLException
    {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(UserRole.fromValue(rs.getString("role")));
                }
            }
        }
        catch (SQLException e)
        {
            throw new SQLException("Error while trying to get user by name", e);
        }

        return user;
    }
}
