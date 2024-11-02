package groovy.org.example;

import io.swagger.model.Task;
import io.swagger.model.User;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        //StartApplication startApplication = new StartApplication();
        //startApplication.run();

        DatabaseManager dbManager = new DatabaseManager();
        dbManager.createUser("john_doe", "User", "john_doe@example.com");
        dbManager.createUser("jane_smith", "User", "jane_smith@example.com");
        dbManager.createUser("dick_smith", "User", "dick_smith@example.com");

        dbManager.createTask("Task 1", "Description for Task 1", "Pending", "High", "john_doe", "jane_smith");
        dbManager.createTask("Task 2", "Description for Task 2", "Resolved", "Medium", "jane_smith", "jane_smith");
        dbManager.createTask("Task 3", "Description for Task 3", "Resolved", "Medium", "dick_smith", "jane_smith");
        dbManager.createTask("Task 4", "Эта хуйня работает?", "Resolved", "Medium", "dick_smith", "jane_smith");

        List<Task> list = dbManager.getTasksByUserName("jane_smith");

        for (Task task: list)
            System.out.println("ОПИСАНИЕ: " + task.getDescription() + ", ID ИСПОЛНИТЕЛЯ: " + dbManager.getUserNameByUserId(task.getAssignee()));

    }
}
