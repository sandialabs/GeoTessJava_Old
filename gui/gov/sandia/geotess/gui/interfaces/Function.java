package gov.sandia.geotess.gui.interfaces;

import gov.sandia.geotess.GeoTessModel;

public interface Function {

	public void execute();
	
	public String methodHelp();

    public void updateModel(GeoTessModel model);
}
