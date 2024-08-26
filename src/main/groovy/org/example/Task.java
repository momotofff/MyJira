package org.example;

public class Task
{
    String name;
    String description;
    StatusTask statusTask;
    PriorityTask priorityTask;

    public Task(String name, String description, PriorityTask priorityTask)
    {
        this.name = name;
        this.description = description;
        this.priorityTask = priorityTask;
    }
}
