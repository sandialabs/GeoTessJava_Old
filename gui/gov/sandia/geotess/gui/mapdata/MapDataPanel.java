package gov.sandia.geotess.gui.mapdata;

import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractMainConsolePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MapDataPanel extends JPanel{

	private MapDataOptionsPanel options;
    private AbstractMainConsolePanel mainPanel;

    public MapDataPanel(GeoTessPresenter presenter, JFrame parent)
    {   this.setLayout(new BorderLayout());
        this.options = new MapDataOptionsPanel(presenter, parent);
        this.mainPanel = new MapDataMainPanel();
        init();
    }

    private void init()
    {
        this.add(options, BorderLayout.WEST);
        this.add(mainPanel);
    }

    public void updateMapDataText(String s)
    {
        mainPanel.updateText(s);
    }

    public void updateFileDisplayPanel(String modelFile, String grid) throws IOException
    {
        options.updateDialogModel(modelFile, grid);
    }
	
}
