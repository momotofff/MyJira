package momotoff.myjira.dbmanager;

import io.swagger.model.UpdateUserRequest;
import io.swagger.model.User;
import io.swagger.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class UsersDbManager
{
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
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys())
                {
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

    public static User updatingUserByName(Connection connection, String username, UpdateUserRequest body)
    {
        String sql = "UPDATE users SET role = ?::userrole, email = ? WHERE username = ?";
        User updatedUser = null;

        if (body == null || body.getRole() == null || body.getEmail() == null)
            throw new IllegalArgumentException("Request body or its properties cannot be null");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {

            preparedStatement.setString(1, body.getRole().getValue());
            preparedStatement.setString(2, body.getEmail());
            preparedStatement.setString(3, username);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                updatedUser = new User();
                updatedUser.setUsername(username);
                updatedUser.setRole(body.getRole());
                updatedUser.setEmail(body.getEmail());
            }
            else
            {
                throw new NoSuchElementException("User with username " + username + " does not exist.");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error updating user: " + e.getMessage());
        }

        return updatedUser;
    }

    public static User updatingUserById(Connection connection, long userId, UpdateUserRequest body)
    {
        String sql = "UPDATE users SET role = ?::userrole, email = ? WHERE id = ?";
        User updatedUser = null;

        if (body == null || body.getRole() == null || body.getEmail() == null)
            throw new IllegalArgumentException("Request body or its properties cannot be null");

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {

            preparedStatement.setString(1, body.getRole().getValue());
            preparedStatement.setString(2, body.getEmail());
            preparedStatement.setLong(3, userId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0)
            {
                updatedUser = new User();
                updatedUser.setId(userId);
                updatedUser.setRole(body.getRole());
                updatedUser.setEmail(body.getEmail());
            }
            else
            {
                throw new NoSuchElementException("User with ID " + userId + " does not exist.");
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error updating user: " + e.getMessage());
        }

        return updatedUser;
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

    public static User getUserById(Connection connection, long id) throws SQLException
    {
        String sql = "SELECT * FROM users WHERE id = ?";

        User user = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {
            pstmt.setLong(1, id);

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
            throw new SQLException("Error while trying to get user by id", e);
        }

        return user;
    }

    public static void deleteUserByName(Connection connection, String userName)
    {
        if (!isUserExists(connection, userName))
            return;

        String sql = "DELETE FROM users WHERE userName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate(); // Выполнение запроса на удаление
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void deleteUserById(Connection connection, Long userId)
    {
        if (!isUserExists(connection, userId))
            return;

        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate(); // Выполнение запроса на удаление
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean isUserExists(Connection connection, String userName)
    {
        String sql = "SELECT COUNT(*) FROM users WHERE userName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                    return resultSet.getInt(1) > 0;
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static boolean isUserExists(Connection connection, Long userId)
    {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                    return resultSet.getInt(1) > 0;
            }
        }

        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return false;
    }
}
