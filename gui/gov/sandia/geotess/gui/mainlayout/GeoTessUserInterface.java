package gov.sandia.geotess.gui.mainlayout;
import gov.sandia.geotess.GeoTessExplorer;
import gov.sandia.geotess.GeoTessModel;
import javax.swing.*;

/**
 * This is the main class for the {@link GeoTessExplorer} user interface.
 * 
 * @author dmdaily
 *
 */
public class GeoTessUserInterface {
	
	/**
	 * This method makes a new {@link Runnable} which makes a {@link GeoTessModel}.
	 * This ensures that the GUI is being run on the Event Dispatching Thread.  
	 */
	public void execute()
	{
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GeoTessUIModel model = new GeoTessUIModel();
                GeoTessPresenter presenter = new GeoTessPresenter();
                model.setPresenter(presenter);
                presenter.setModel(model);
				model.startApplication();
			}
			
		});
	}
	
	/**
	 * Main class that makes a new GeoTessUserInterface and calls execute on the object.  
	 * When execute is called, the GUI begins getting initialized.
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		GeoTessUserInterface geo = new GeoTessUserInterface();
		geo.execute();
	}
	
	
}
