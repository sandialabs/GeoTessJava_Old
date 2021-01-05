package gov.sandia.geotess.gui.tools;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class AbstractNoModelNeeded extends AbstractDialogFunction{


	public AbstractNoModelNeeded(GeoTessPresenter presenter, JFrame parent, String title) {
		super(presenter, parent, title);
	}

    @Override
    public JPanel initialPanel() {
        return makeMainPanelNoModel();
    }

	@Override
	public abstract ActionListener getAcceptButtonListener();

    public abstract JPanel makeMainPanelNoModel();

	@Override
	public JPanel makeNewDialog(GeoTessModel model)
    {
        return makeMainPanelNoModel();
    }
}
