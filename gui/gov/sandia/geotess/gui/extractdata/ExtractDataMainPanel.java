package gov.sandia.geotess.gui.extractdata;

import gov.sandia.geotess.gui.tools.AbstractMainConsolePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class ExtractDataMainPanel extends AbstractMainConsolePanel {

    public ExtractDataMainPanel() {
        super();
    }


    @Override
    public JPanel mainPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.RED);
        return p;
    }
}
