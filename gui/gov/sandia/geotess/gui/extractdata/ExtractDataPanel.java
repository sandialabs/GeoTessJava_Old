package gov.sandia.geotess.gui.extractdata;

import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractMainConsolePanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ExtractDataPanel extends JPanel{

	private ExtractDataOptionsPanel options;
    private AbstractMainConsolePanel mainPanel;

    public ExtractDataPanel(GeoTessPresenter presenter, JFrame parent)
	{   this.setLayout(new BorderLayout());
		this.options = new ExtractDataOptionsPanel(presenter, parent);
        this.mainPanel = new ExtractDataMainPanel();
        init();
	}

    private void init()
    {
        this.add(options, BorderLayout.WEST);
        this.add(mainPanel);
    }

    public void updateExtractDataText(String s)
    {
        mainPanel.updateText(s);
    }

	public void updateFileDisplayPanel(String modelFile, String grid) throws IOException
	{
		options.updateDialogModel(modelFile, grid);
	}
}
