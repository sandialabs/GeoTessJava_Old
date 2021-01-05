package gov.sandia.geotess.gui.mapdata;

import gov.sandia.geotess.gui.tools.AbstractMainConsolePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class MapDataMainPanel extends AbstractMainConsolePanel{

    public MapDataMainPanel()
    {
        super();
    }

    @Override
    public JPanel mainPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.CYAN);
        return p;
    }
}

