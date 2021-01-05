package gov.sandia.geotess.gui.utilities;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.interfaces.Function;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import java.io.IOException;

public class Statistics implements Function {

	private GeoTessPresenter presenter;

	public Statistics(GeoTessPresenter presenter) {
		this.presenter = presenter;
	}

    @Override
    public void updateModel(GeoTessModel model) {
    }

    @Override
	public void execute() {
		if (!presenter.hasModel())
			presenter.writeToUtilityPanel("Error please enter a Model before accessing this function");
		else {
			try {
				presenter.makeStatistics();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String methodHelp() {
		return MethodHelp.STATISTICS.getMethodTip();
	}
}
