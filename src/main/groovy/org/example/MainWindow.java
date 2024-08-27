package org.example;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame
{
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;


    public MainWindow()
    {
        setTitle("MyJira");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setJMenuBar(createMenuBar(new JMenuBar()));

        JButton b1 = new JButton("Тест кнопка 1"); //создаем обьект Кнопки
        b1.setLocation(10, 10); //задаем располажение кнопки
        b1.setSize(50, 50); //задаем размер кнопки(ширина-высота)
        add(b1); //добавляем кнопку в JPanel panel

        JButton b2 = new JButton("Тест кнопка 2"); //создаем обьект Кнопки
        b2.setLocation(100, 100); //задаем располажение кнопки
        b2.setSize(50, 50); //задаем размер кнопки
        add(b2); //добавляем кнопку в JPanel panel
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
