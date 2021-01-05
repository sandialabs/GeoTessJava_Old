package gov.sandia.geotess.gui.mainlayout;

import javax.swing.*;
import java.io.IOException;

public class MainFrame extends JFrame
{
	private GeoTessPresenter presenter;
	private CustomTabs tabs;

	public MainFrame(GeoTessPresenter presenter, String version)
	{
		this.presenter = presenter;
		this.setTitle("GeoTess Java User Interface Version " + version);
		init();
	}

    public void setPresenter(GeoTessPresenter presenter)
    {
        this.presenter = presenter;
    }

	private void init()
	{
		try
		{UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");	
		}
		catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		tabs = new CustomTabs(presenter, this);
		this.add(tabs);
	}

    public void updateFileDisplayPanel(String modelFile, String gridDir) throws IOException{
        tabs.updateFileDisplayPanel(modelFile, gridDir);
    }

	public void updateUtilityText(String s)
	{
		tabs.updateUtilityText(s);
	}

    public void updateExtractDataText(String s)
    {
        tabs.updateExtractDataText(s);
    }

    public void updateMapDataText(String s)
    {
        tabs.updateMapDataText(s);
    }

	public void clearUtilityText()
	{
		tabs.clearUtilityText();
	}

}
