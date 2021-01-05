package gov.sandia.geotess.gui.mapdata.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class VTK3DBlockDialog extends AbstractModelNeededDialog {

	private FileIOComponents output;
	private TitleFieldComponents firstLat;
	private TitleFieldComponents lastLat;
	private DeltaOrNComponents latSpacing;
	private TitleFieldComponents firstLon;
	private TitleFieldComponents lastLon;
	private DeltaOrNComponents lonSpacing;
	private DeepestShallowestComponents layers;
	private RadioButtonPanel radialDimension;
	private TitleFieldComponents maxRadialSpacing;
	private HorizontalInterpolationComponents horizontalInterpolation;
	private RadialInterpolationComponents radialInterpolation;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;

	public VTK3DBlockDialog(GeoTessPresenter presenter, JFrame parent,
			String title) {
		super(presenter, parent, title);
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		this.output = new FileIOComponents("Output File: ", FileOperation.SAVE,
				this, ParameterHelp.OUTPUT);
		this.firstLat = new TitleFieldComponents("First Latitude: ", 4,
				"degrees", ParameterHelp.FIRST_LAT);
		this.lastLat = new TitleFieldComponents("First Latitude: ", 4,
				"degrees", ParameterHelp.LAST_LAT);
		this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");

		this.firstLon = new TitleFieldComponents("First Longitude: ", 4,
				"degrees", ParameterHelp.FIRST_LAT);
		this.lastLon = new TitleFieldComponents("Last Longitude: ", 4,
				"degrees", ParameterHelp.LAST_LAT);
		this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");

		this.layers = new DeepestShallowestComponents(model);
		this.radialDimension = new RadioButtonPanel("Depth", "Radius",
				"LayerIndex");
		this.maxRadialSpacing = new TitleFieldComponents(
				"Maximum Radial Spacing: ", 4, ParameterHelp.MAX_RADIAL_SPACING);
		this.horizontalInterpolation = new HorizontalInterpolationComponents();
		this.radialInterpolation = new RadialInterpolationComponents();
		this.reciprocal = new ReciprocalComponents();
		this.attributes = new AttributeCheckboxPanel(model);

		p.add(getTitleFieldComponents(firstLat));
		p.add(getTitleFieldComponents(lastLat));
		p.add(latSpacing);
		p.add(getTitleFieldComponents(firstLon));
		p.add(getTitleFieldComponents(lastLon));
		p.add(lonSpacing);

		p.add(layers.getDeepLabel());
		p.add(layers.getDeepBox());

		p.add(layers.getShallowLabel());
		p.add(layers.getShallowBox());

		p.add(new PopupLabel("Radial Dimesnion: ",
				ParameterHelp.RADIAL_DIMENSION));
		p.add(radialDimension);

		p.add(getTitleFieldComponents(maxRadialSpacing));

		p.add(horizontalInterpolation.getTitle());
		p.add(horizontalInterpolation.getButtons());
		p.add(radialInterpolation.getTitle());
		p.add(radialInterpolation.getButtons());

		p.add(reciprocal.getButtons());
		p.add(attributes);

		p.add(output.getTitle());
		p.add(output.getTextBox());
		p.add(output.getButton(), "wrap");

		return p;
	}

	private JPanel getTitleFieldComponents(TitleFieldComponents components) {
		JPanel p = new JPanel();
		p.add(components.getTitle());
		p.add(components.getTextBox());
		return p;
	}

	@Override
	public ActionListener getAcceptButtonListener() {

		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					presenter.vtk3dBlock(firstLat.getFieldValue(), lastLat
							.getFieldValue(), latSpacing.getTextField()
							.getText(), firstLon.getFieldValue(), lastLon
							.getFieldValue(), lonSpacing.getTextField()
							.getText(), layers.getDeepestIndex(), layers
							.getShallowestIndex(), radialDimension
							.getSelected(), Double.parseDouble(maxRadialSpacing
							.getFieldValue()), horizontalInterpolation
							.getInterpolation(), radialInterpolation
							.getInterpolation(), reciprocal.getSelected(),
							attributes.getCheckedAttributeIndexes(), output
									.getText());
					destroy();

				} catch (Exception ee) {
					setErrorVisible(true);
				}

			}
		};
	}

	@Override
	public String methodHelp() {
		return MethodHelp.VTK_3D_BLOCK.getMethodTip();
	}

}
