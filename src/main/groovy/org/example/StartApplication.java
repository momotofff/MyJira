package org.example;

import javax.swing.*;
import java.awt.*;

public class StartApplication  implements Runnable
{
    MainWindow frame = new MainWindow();

    public StartApplication()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        EventQueue.invokeLater(() -> {});
    }

    @Override
    public void run()
    {
        frame.update();
    }
}
