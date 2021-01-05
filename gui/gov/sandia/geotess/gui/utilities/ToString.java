package gov.sandia.geotess.gui.utilities;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

public class ToString implements Function {

	private GeoTessPresenter presenter;

	public ToString(GeoTessPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void updateModel(GeoTessModel model) {
    }

    @Override
	public void execute() {
		try {
			if (!presenter.hasModel())
				presenter.writeToUtilityPanel("Error please enter a Model before accessing this function");
			else
				presenter.makeToString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String methodHelp() {
		return MethodHelp.TO_STRING.getMethodTip();
	}

}
