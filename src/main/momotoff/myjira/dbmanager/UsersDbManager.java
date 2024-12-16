package momotoff.myjira.dbmanager;

import io.swagger.api.UsersApiController;
import io.swagger.model.UpdateUserRequest;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

class UsersDbManager
{
    private static final Logger logger = LoggerFactory.getLogger(UsersDbManager.class);

    public static User createUser(Connection connection, String username, String role, String email) throws SQLException
    {
        if (!isValidEmail(email))
            throw new SQLException("Invalid email: " + email);

        if (!isValidRole(role))
            throw new SQLException("Invalid role: " + role);


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
                        logger.info("User with ID = %d created successfully!%n" + id);
                    }
                }
            }
        }
        catch (SQLException e)
        {
            String sqlState = e.getSQLState();                                              // Проверяем код SQL состояния для определения причины ошибки

            if ("23505".equals(sqlState))                                                   // Код ошибки для нарушения уникального ограничения для PostgreSQL
                throw new SQLException("Username already exists: " + username, e);

            else
                throw new SQLException("Error creating user: " + username, e);
        }

        return user;
    }

    public static User updatingUserByName(Connection connection, String username, UpdateUserRequest body) throws SQLException
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
            throw new SQLException("Error updating user", e);
        }

        return updatedUser;
    }

    public static User updatingUserById(Connection connection, long userId, UpdateUserRequest body) throws SQLException
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
            throw new SQLException("Error updating user", e);
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

    public static void deleteUserByName(Connection connection, String userName) throws SQLException
    {
        String sql = "DELETE FROM users WHERE userName = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new SQLException("Error while trying to delete user by name", e);
        }
    }

    public static void deleteUserById(Connection connection, Long userId) throws SQLException
    {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new SQLException("Error while trying to delete user by id", e);
        }
    }

    public static boolean isUserExists(Connection connection, String userName) throws SQLException
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
            throw new SQLException("Error while trying to check user by name", e);
        }

        return false;
    }

    public static boolean isUserExists(Connection connection, Long userId) throws SQLException
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
            throw new SQLException("Error while trying to check user by id", e);
        }

        return false;
    }

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static boolean isValidRole(String role) {
        try {
            UserRole.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
