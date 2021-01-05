package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.DeltaOrNComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class MapLayerThicknessDialog extends AbstractModelNeededDialog {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;

	private DeltaOrNComponents latSpacing;
	private DeltaOrNComponents lonSpacing;

	private HorizontalInterpolationComponents horizontal;
	private TitleFieldComponents firstLayerID;
	private TitleFieldComponents lastLayerID;

	private FileIOComponents output;
	
	public MapLayerThicknessDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Map Thickness Dialog");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "[][]10[]"));

		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,
				parent, ParameterHelp.OUTPUT);
		this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
		this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
		this.horizontal = new HorizontalInterpolationComponents();
		this.latlon1 = new LatLonComponents("First Latitude: ",
				"First Longitude: ");
		this.latlon2 = new LatLonComponents("Last Latitude: ",
				"Last Longitude: ");
		this.firstLayerID = layerIDPanel("First Layer ID: ",
				ParameterHelp.FIRST_LAYER_ID);
		this.lastLayerID = layerIDPanel("Last Layer ID: ",
				ParameterHelp.LAST_LAYER_ID);
		
		panel.add(latlon1.getLatTitle());
		panel.add(latlon1.getLatTextBox(), "split 2");
		panel.add(latlon1.getLatUnits());

		panel.add(latlon2.getLatTitle());
		panel.add(latlon2.getLatTextBox(), "split 2");
		panel.add(latlon2.getLatUnits(), "wrap");

		panel.add(latSpacing.getTitle());
		panel.add(latSpacing.getButtons());
		panel.add(latSpacing.getTextField(), "wrap");

		panel.add(latlon1.getLonTitle());
		panel.add(latlon1.getLonTextBox(), "split 2");
		panel.add(latlon1.getLonUnits());

		panel.add(latlon2.getLonTitle());
		panel.add(latlon2.getLonTextBox(), "split 2");
		panel.add(latlon2.getLonUnits(), "wrap");

		panel.add(lonSpacing.getTitle());
		panel.add(lonSpacing.getButtons());
		panel.add(lonSpacing.getTextField(), "wrap");

		panel.add(firstLayerID.getTitle());
		panel.add(firstLayerID.getTextBox());

		panel.add(lastLayerID.getTitle());
		panel.add(lastLayerID.getTextBox(), "wrap");

		panel.add(horizontal.getTitle());
		panel.add(horizontal.getButtons(), "wrap");

		panel.add(output.getTitle());
		panel.add(output.getTextBox(), "spanx 2");
		panel.add(output.getButton(), "wrap");
	
		
		return panel;
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String lat1 = String.valueOf(latlon1.getLat());
					String lat2 = String.valueOf(latlon2.getLat());
					String lon1 = String.valueOf(latlon1.getLon());
					String lon2 = String.valueOf(latlon2.getLon());
					presenter.mapLayerThickness(lat1, lat2, latSpacing
							.getTextField().getText(), lon1, lon2, lonSpacing
							.getTextField().getText(), Integer
							.parseInt(firstLayerID.getFieldValue()), Integer
							.parseInt(lastLayerID.getFieldValue()), horizontal.getInterpolation(),
							output.getText());

					destroy();
					
				} catch (IOException | GeoTessException | NumberFormatException ee) {
					setErrorVisible(true);
				}
			}
		};

	}

	@Override
	public String methodHelp() {
		return MethodHelp.MAP_LAYER_THICKNESS.getMethodTip();
	}

	private TitleFieldComponents layerIDPanel(String s, ParameterHelp help) {
		return new TitleFieldComponents(s, 4, help);
	}

}
