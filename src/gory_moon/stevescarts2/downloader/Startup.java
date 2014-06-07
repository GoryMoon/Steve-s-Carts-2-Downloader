package gory_moon.stevescarts2.downloader;

import javax.swing.*;
import java.awt.*;

public class Startup extends JFrame {

    private JProgressBar startupBar;
    private JPanel jPanel1;
    private JLabel loadingLabel;

    public Startup() {
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setTitle("Starting");
        setResizable(false);
        setContentPane(jPanel1);
        setVisible(true);
        startupBar.setStringPainted(false);
        pack();
        setLocationRelativeTo(null);
    }

    public Startup setProgressText(String s) {
        loadingLabel.setText(s);
        return this;
    }

    public Startup setUnknownTime(boolean bol) {
        startupBar.setIndeterminate(bol);
        return this;
    }
}
