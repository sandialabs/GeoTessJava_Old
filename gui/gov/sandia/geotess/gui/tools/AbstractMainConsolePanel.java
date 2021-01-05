package gov.sandia.geotess.gui.tools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by dmdaily on 7/29/2014.
 */
public abstract class AbstractMainConsolePanel extends JPanel{

    private JTextArea consoleText;

    public AbstractMainConsolePanel() {
        this.setLayout(new BorderLayout());
        this.add(mainPanel(), BorderLayout.CENTER);
        this.add(console(), BorderLayout.SOUTH);
    }

    private JPanel console()
    {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new TitledBorder("Console"));

        consoleText = new JTextArea();
        consoleText.setFont(new Font("Courier", 0, 12));
        consoleText.setPreferredSize(new Dimension(200,200));
        consoleText.setEditable(false);
        consoleText.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(consoleText);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    public void updateText(String s)
    {
        consoleText.setText(s);
    }

    public abstract JPanel mainPanel();
}
