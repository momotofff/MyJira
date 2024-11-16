package groovy.org.example;

import momotoff.myjira.dbmanager.DatabaseManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.SQLException;

public class Main
{
    public static void main(String[] args) throws SQLException
    {
        //StartApplication startApplication = new StartApplication();
        //startApplication.run();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("john_doe", "password");

        SecurityContextHolder.getContext().setAuthentication(auth);

        DatabaseManager dbManager = new DatabaseManager("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");

        //dbManager.createUser("john_doe", "User", "john_doe@example.com");
        //dbManager.createUser("jane_smith", "User", "jane_smith@example.com");
        //dbManager.createUser("dick_smith", "User", "dick_smith@example.com");

        dbManager.createTask("Tas2324", "Description for Task 1", "Pending", "High", 1L);
        dbManager.createTask("Task 2545", "Description for Task 2", "Resolved", "Medium", 1L);
    }
}
