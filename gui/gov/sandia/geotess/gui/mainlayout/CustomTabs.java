package gov.sandia.geotess.gui.mainlayout;

import gov.sandia.geotess.gui.extractdata.ExtractDataPanel;
import gov.sandia.geotess.gui.mapdata.MapDataPanel;
import gov.sandia.geotess.gui.utilities.UtilityPanel;

import javax.swing.*;
import java.io.IOException;


public class CustomTabs extends JTabbedPane{

	private UtilityPanel utility;
	private ExtractDataPanel extract;
	private MapDataPanel mapData;
	
	
	public CustomTabs(GeoTessPresenter presenter, JFrame parent)
	{
		this.utility = new UtilityPanel(presenter, parent);
		this.extract = new ExtractDataPanel(presenter, parent);
		this.mapData = new MapDataPanel(presenter, parent);
		init();
	}
	
	private void init()
	{
		this.addTab("Utilities", utility);
		this.addTab("Extract Data", extract);
		this.addTab("Map Data", mapData);
	}

    public void updateFileDisplayPanel(String modelFile, String gridDir) throws IOException{
            utility.updateFileDisplayPanel(modelFile, gridDir);
            extract.updateFileDisplayPanel(modelFile, gridDir);
            mapData.updateFileDisplayPanel(modelFile, gridDir);
    }

    public void updateUtilityText(String s)
	{
		utility.updateUtilityText(s);
	}

    public void updateExtractDataText(String s)
    {
        extract.updateExtractDataText(s);
    }

    public void updateMapDataText(String s)
    {
        mapData.updateMapDataText(s);
    }

	public void clearUtilityText()
	{
		utility.clearUtilityText();
	}

}
