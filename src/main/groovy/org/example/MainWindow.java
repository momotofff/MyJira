package org.example;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame
{
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 300;
    private JButton b1;
    private JButton b2;


    public MainWindow()
    {
        setTitle("MyJira");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setJMenuBar(createMenuBar(new JMenuBar()));

        Container container = new Container();
        b1 = new JButton("Тест кнопка 1");
        b1.setLocation(10, 10);
        b1.setSize(150, 50);
        container.add(b1);

        b2 = new JButton("Тест кнопка 2");
        b2.setLocation(170, 10);
        b2.setSize(150, 50);
        container.add(b2);
        add(container);
    }

    public void update()
    {

    }

    private JMenuBar createMenuBar(JMenuBar menuBar)
    {
        JButton signIn = new JButton("Вход");
        JButton signUp = new JButton("Регистрация");
        JButton signOut = new JButton("Выход");
        menuBar.add(signIn);
        menuBar.add(signUp);
        menuBar.add(signOut);

        return menuBar;
    }
}
