package org.example;

public class Main
{
    public static void main(String[] args)
    {
        StartApplication startApplication = new StartApplication();
        startApplication.run();

        DatabaseManager dbManager = new DatabaseManager();

        // Создание нового пользователя
        dbManager.createUser("новый_пользователь", "editor", "user@example.com");

        // Создание новой задачи
        dbManager.createTask("Задача 1", "Описание задачи 1", "pending", "high", "Автор", "Исполнитель");
    }
}
