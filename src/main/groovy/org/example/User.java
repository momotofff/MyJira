package org.example;

public class User
{
    String name;
    String jobTitle;
    String token;
    Task task;

    public User() {}

    public User(String name, String jobTitle)
    {
        this.name = name;
        this.jobTitle = jobTitle;
    }
}
