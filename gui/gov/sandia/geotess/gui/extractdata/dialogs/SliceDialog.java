package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SliceDialog extends AbstractModelNeededDialog {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;
	private boolean shortestPath;
	private TitleFieldComponents nPoints;
	private TitleFieldComponents maxRadSpacing;
    private DeepestShallowestComponents layers;
	private HorizontalInterpolationComponents horizontal;
    private RadialInterpolationComponents radial;
	private String spatialCoords;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;
	private FileIOComponents output;

	public SliceDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Slice");
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new SubmitButtonListener();
	}

	@Override
	public String methodHelp() {
		return MethodHelp.SLICE.getMethodTip();

	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        this.layers = new DeepestShallowestComponents(model);
        this.reciprocal = new ReciprocalComponents();
        this.horizontal = new HorizontalInterpolationComponents();
        this.radial = new RadialInterpolationComponents();
        
		p.add(latlonPanel());
		p.add(shortestPath());
		p.add(nPointsPanel());
		p.add(maximumRadialSpacingPanel());

        p.add(layers.getDeepLabel());
        p.add(layers.getDeepBox());

        p.add(layers.getShallowLabel());
        p.add(layers.getShallowBox());
        p.add(reciprocal.getButtons());

        p.add(horizontal.getTitle());
        p.add(horizontal.getButtons());
        p.add(radial.getTitle());
        p.add(radial.getButtons());

        p.add(spatialCoordinates());

		p.add(attributes(model));

        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
		p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton(), "wrap");
        
		return p;
	}
	
	private JPanel attributes(GeoTessModel model) {
		return attributes = new AttributeCheckboxPanel(model);
	}

	private JPanel spatialCoordinates() {
		final String[] coordinates = { "Distance", "Depth", "Radius", "X", "Y","Z", "Lat", "Lon" };

		JPanel p = new JPanel();
		ButtonGroup bg = new ButtonGroup();
		ActionListener al = new SpatialCoordinatesListener();
		for (String s : coordinates) {
			JRadioButton button = new JRadioButton(s);
			button.addActionListener(al);
			if(s.equalsIgnoreCase("distance")){
				button.setSelected(true);
				spatialCoords = "Distance";
			}
			bg.add(button);
			p.add(button);
		}
		return p;
	}

    private JPanel maximumRadialSpacingPanel(){
        JPanel p = new JPanel();
        this.maxRadSpacing = new TitleFieldComponents("Max Radial Spacing: ", 4, ParameterHelp.MAX_RADIAL_SPACING);
        p.add(maxRadSpacing.getTitle());
        p.add(maxRadSpacing.getTextBox());
        return p;
    }

    private JPanel nPointsPanel() {
        JPanel p = new JPanel();
        this.nPoints = new TitleFieldComponents("N Points: ", 4, ParameterHelp.N_POINTS);
        p.add(nPoints.getTitle());
        p.add(nPoints.getTextBox());
        return p;
    }

	private JPanel shortestPath() {
		JPanel p = new JPanel();
		JLabel label = new PopupLabel("Shortest Path", ParameterHelp.SHORTEST_PATH);
		p.add(label);

		ButtonGroup bg = new ButtonGroup();
		JRadioButton trueButton = new JRadioButton("True");
		JRadioButton falseButton = new JRadioButton("False");

		ActionListener al = new ShortestPathListener();
		trueButton.addActionListener(al);
		falseButton.addActionListener(al);

		bg.add(trueButton);
		bg.add(falseButton);

		p.add(trueButton);
		p.add(falseButton);

		return p;

	}

    private JPanel latlonPanel()
    {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4,3,5,5));
        this.latlon1 = new LatLonComponents("Latitude 1: ", "Longitude 1: ");
        this.latlon2 = new LatLonComponents("Latitude 2: ", "Longitude 2: ");

        p.add(latlon1.getLatTitle());
        p.add(latlon1.getLatTextBox());

        p.add(latlon1.getLonTitle());
        p.add(latlon1.getLonTextBox());

        p.add(latlon2.getLatTitle());
        p.add(latlon2.getLatTextBox());

        p.add(latlon2.getLonTitle());
        p.add(latlon2.getLonTextBox());

        return p;
    }


	private class ShortestPathListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String choosen = ((JRadioButton) e.getSource()).getText();
			shortestPath = Boolean.parseBoolean(choosen);
		}
	}
	
	private class SpatialCoordinatesListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			spatialCoords = ((JRadioButton) e.getSource()).getText();
		}
		
	}

	private class SubmitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
						
			try {
				presenter.slice(latlon1.getLat(), latlon1.getLon(),
						latlon2.getLat(), latlon2.getLon(), shortestPath,
						Integer.parseInt(nPoints.getFieldValue()), Double.parseDouble(maxRadSpacing.getFieldValue()),
						layers.getDeepestIndex(),layers.getShallowestIndex(),
						horizontal.getInterpolation(),
						radial.getInterpolation(), spatialCoords,
                        reciprocal.getSelected(),
						attributes.getCheckedAttributeIndexes(), output.getText());
				destroy();
			} catch (Exception e) {
				setErrorVisible(true);
			}
		}
	}
}
