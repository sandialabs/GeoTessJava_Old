package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeepestShallowestComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class BoreholeDialog extends AbstractModelNeededDialog {

	private LatLonComponents latComponents;
	private LatLonComponents lonComponents;
	private TitleFieldComponents radSpacingComponents;
	private DeepestShallowestComponents layers;
	private HorizontalInterpolationComponents horComponents;
	private RadialInterpolationComponents radComponents;
	private ReciprocalComponents reciprocalComponents;
	private RadioButtonPanel depthRad;
	private AttributeCheckboxPanel attributes;

	public BoreholeDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Borehole Dialog");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));

		this.latComponents = new LatLonComponents();
		this.lonComponents = new LatLonComponents();
		this.radSpacingComponents = new TitleFieldComponents("Max Radial Spacing: ", 5, " Km",ParameterHelp.MAX_RADIAL_SPACING);
		this.horComponents = new HorizontalInterpolationComponents();
		this.radComponents = new RadialInterpolationComponents();
		this.reciprocalComponents = new ReciprocalComponents();
		this.depthRad = new RadioButtonPanel("Depth", "Radius");
		this.attributes = new AttributeCheckboxPanel(model);
		this.attributes.setPreferredSize(new Dimension(200, 200));
		this.layers = new DeepestShallowestComponents(model);

		p.add(latComponents.getLatTitle());
		p.add(latComponents.getLatTextBox(), "split 2");
		p.add(latComponents.getLatUnits());
		p.add(layers.getDeepLabel());
		p.add(layers.getDeepBox(), "wrap");
		p.add(lonComponents.getLonTitle());
		p.add(lonComponents.getLonTextBox(), "split 2");
		p.add(lonComponents.getLonUnits());
		p.add(layers.getShallowLabel());
		p.add(layers.getShallowBox(), "wrap");
		p.add(radSpacingComponents.getTitle());
		p.add(radSpacingComponents.getTextBox());
		p.add(attributes, "span 2 6, wrap");
		p.add(horComponents.getTitle());
		p.add(horComponents.getButtons(), "wrap");
		p.add(radComponents.getTitle());
		p.add(radComponents.getButtons(), "wrap");

		// Adds the Depth and Radius Buttons
		p.add(new PopupLabel("Depth or Radius: ", ParameterHelp.DEPTH_OR_RADIUS));
		p.add(new RadioButtonPanel("Depth", "Radius"), "wrap");
		p.add(reciprocalComponents.getTitle());
		p.add(reciprocalComponents.getButtons(), "wrap");

		p.add(horComponents.getTitle());
		p.add(horComponents.getButtons(), "wrap");
		p.add(radComponents.getTitle());
		p.add(radComponents.getButtons(), "wrap");

		// Adds the Depth and Radius Buttons
		p.add(new PopupLabel("Depth or Radius: ", ParameterHelp.DEPTH_OR_RADIUS));
		p.add(new RadioButtonPanel("Depth", "Radius"), "wrap");
		p.add(reciprocalComponents.getTitle());
		p.add(reciprocalComponents.getButtons(), "wrap");

		return p;
	}

	@Override
	public String methodHelp() {
		return MethodHelp.BOREHOLE.getMethodTip();
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					double radSpace = Double.parseDouble(radSpacingComponents
							.getFieldValue());
					presenter.borehole(latComponents.getLat(),
							lonComponents.getLon(), radSpace,
							layers.getDeepestIndex(),
							layers.getShallowestIndex(),
							horComponents.getInterpolation(),
							radComponents.getInterpolation(),
							depthRad.getSelected(),
							reciprocalComponents.getSelected(),
							attributes.getCheckedAttributeIndexes());
					destroy();
				} catch (GeoTessException | IOException | NumberFormatException e) {
					setErrorVisible(true);
				}
			}
		};
	}
}
