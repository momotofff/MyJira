package groovy.org.example;

import io.swagger.model.User;

public class Main
{
    public static void main(String[] args)
    {
        //StartApplication startApplication = new StartApplication();
        //startApplication.run();

        DatabaseManager dbManager = new DatabaseManager();

        dbManager.createUser("john_doe", "User", "john_doe@example.com");
        dbManager.createUser("jane_smith", "User", "jane_smith@example.com");

        dbManager.createTask("Task 1", "Description for Task 1", "pending", "high", "john_doe", "jane_smith");
        dbManager.createTask("Task 2", "Description for Task 2", "in_progress", "medium", "jane_smith", null);
    }
}
