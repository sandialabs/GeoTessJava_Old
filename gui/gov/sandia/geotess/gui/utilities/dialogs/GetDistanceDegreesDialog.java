package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractNoModelNeeded;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class GetDistanceDegreesDialog extends AbstractNoModelNeeded {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;
	private TitleFieldComponents nPoints;

	public GetDistanceDegreesDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Distance Degrees");
	}

	@Override
	public JPanel makeMainPanelNoModel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));
		p.add(latlonPanel(), "wrap");
		p.add(nPointPanel(), "center, wrap");
		return p;
	}

	private JPanel nPointPanel() {
		JPanel p = new JPanel();
		this.nPoints = new TitleFieldComponents("Enter n Points: ", 7,
				ParameterHelp.N_POINTS);
		p.add(nPoints.getTitle());
		p.add(nPoints.getTextBox());
		return p;
	}

	private JPanel latlonPanel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[][]15[]"));
		this.latlon1 = new LatLonComponents("Latitude 1: ", "Longitude 1: ");
		this.latlon2 = new LatLonComponents("Latitude 2: ", "Longitude 2: ");

		p.add(latlon1.getLatTitle());
		p.add(latlon1.getLatTextBox(), "split 2");
		p.add(latlon1.getLatUnits());

		p.add(latlon1.getLonTitle());
		p.add(latlon1.getLonTextBox(), "split 2");
		p.add(latlon1.getLonUnits(), "wrap");

		p.add(latlon2.getLatTitle());
		p.add(latlon2.getLatTextBox(), "split 2");
		p.add(latlon2.getLatUnits());

		p.add(latlon2.getLonTitle());
		p.add(latlon2.getLonTextBox(), "split 2");
		p.add(latlon2.getLonUnits(), "wrap");

		return p;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.GET_DISTANCE_DEGREES.getMethodTip();

	}

	private class SubmitButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int points = Integer.parseInt(nPoints.getFieldValue().trim());
			presenter.getDistanceDegrees(latlon1.getLat(), latlon1.getLon(),
					latlon2.getLat(), latlon2.getLon(), points);
			destroy();

		}
	}
}
