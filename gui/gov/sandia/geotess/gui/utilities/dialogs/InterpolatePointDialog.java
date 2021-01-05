package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class InterpolatePointDialog extends AbstractModelNeededDialog {
	private HorizontalInterpolationComponents horizontal;
	private RadialInterpolationComponents radial;
	private ReciprocalComponents reciprocal;
	private LatLonComponents latlon;
	private TitleFieldComponents depth;
	private TitleFieldComponents layer;
	
	public InterpolatePointDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Interpolate Point");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		this.depth = new TitleFieldComponents("Depth: ", 7, ParameterHelp.DEPTH);
		this.layer = new TitleFieldComponents("Layer: ", 7,ParameterHelp.LAYER_ID);
		this.horizontal = new HorizontalInterpolationComponents();
		this.radial = new RadialInterpolationComponents();
		this.reciprocal = new ReciprocalComponents();
		p.add(fieldPanel(), "wrap");
		p.add(radioPanel(),"wrap");

		return p;
	}

	private JPanel radioPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new MigLayout());
		p.add(horizontal.getTitle());
		p.add(horizontal.getButtons(), "wrap");

		p.add(radial.getTitle());
		p.add(radial.getButtons(), "wrap");
		
		p.add(reciprocal.getTitle());
		p.add(reciprocal.getButtons(), "wrap");
		return p;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.INTERPOLATE_POINT.getMethodTip();
	}

	private JPanel fieldPanel() {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill", "[][]20[]"));
		this.latlon = new LatLonComponents();
		p.add(latlon.getLatTitle());
		p.add(latlon.getLatTextBox(), "split 2");
		p.add(latlon.getLatUnits());
		
		
		p.add(latlon.getLonTitle());
		p.add(latlon.getLonTextBox(), "split 2");
		p.add(latlon.getLonUnits(), "wrap");
		
		p.add(depth.getTitle());
		p.add(depth.getTextBox(), "split 2");
		p.add(depth.getUnits());
	
		
		p.add(layer.getTitle());
		p.add(layer.getTextBox(), "split 2");
		p.add(layer.getUnits(), "wrap");
		return p;
	}

	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				presenter.interpolatePoint(latlon.getLat(), latlon.getLon(),
						Double.parseDouble(depth.getFieldValue()),
						Integer.parseInt(layer.getFieldValue()),
						horizontal.getInterpolation(),
						radial.getInterpolation(), reciprocal.getSelected());
				destroy();
			} catch (GeoTessException | IOException | NumberFormatException e) {
				setErrorVisible(true);
			}


		}
	}

}
