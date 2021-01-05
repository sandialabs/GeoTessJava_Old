package gov.sandia.geotess.gui.extractdata.dialogs;

import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapValuesLayerDialog extends AbstractModelNeededDialog {

	private TitleFieldComponents lat1;
	private TitleFieldComponents lat2;
	private TitleFieldComponents lon1;
	private TitleFieldComponents lon2;

	private DeltaOrNComponents latSpacing;
	private DeltaOrNComponents lonSpacing;

	private HorizontalInterpolationComponents horizontal;
    private RadialInterpolationComponents radial;
	private ReciprocalComponents reciprocal;
	private AttributeCheckboxPanel attributes;

	private TitleFieldComponents layerID;
	private TitleFieldComponents fractionalRadius;
	
	private FileIOComponents output;
	
	public MapValuesLayerDialog(GeoTessPresenter presenter, JFrame parent) {
		super(presenter, parent, "Map Values Layer Dialog");
	}

	@Override
	public JPanel makeNewDialog(GeoTessModel model) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.output = new FileIOComponents("Output File: ",FileOperation.SAVE,parent, ParameterHelp.OUTPUT);
        this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
        this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
        this.layerID = new TitleFieldComponents("Layer ID: ", 4, ParameterHelp.LAYER_ID);
        this.fractionalRadius = new TitleFieldComponents("Fractional Radius: ", 7, ParameterHelp.FRAC_RADIUS);
        this.reciprocal = new ReciprocalComponents();
        this.horizontal = new HorizontalInterpolationComponents();
        this.radial = new RadialInterpolationComponents();
        panel.add(lats());
        panel.add(latSpacing);
        panel.add(lons());
        panel.add(lonSpacing);
        panel.add(getTitleFieldPanel(layerID));
        panel.add(getTitleFieldPanel(fractionalRadius));

        panel.add(horizontal.getTitle());
        panel.add(horizontal.getButtons());
        panel.add(radial.getTitle());
        panel.add(radial.getButtons());

        panel.add(reciprocal.getButtons());
        panel.add(attributesPanel(model));

        panel.add(output.getTitle());
        panel.add(output.getTextBox());
        panel.add(output.getButton(), "wrap");

        return panel;
    }

	private JPanel getTitleFieldPanel(TitleFieldComponents components) {
		JPanel p = new JPanel();
        p.add(components.getTitle());
        p.add(components.getTextBox());
        return p;
	}

	private JPanel attributesPanel(GeoTessModel model) {
		return attributes = new AttributeCheckboxPanel(model);
	}


    private JPanel lons() {
        JPanel p = new JPanel();
        this.lon1 = new TitleFieldComponents("First Longitude: ",7, "degrees", ParameterHelp.FIRST_LON);
        this.lon2 = new TitleFieldComponents("Last Longitude: ", 7, "degrees", ParameterHelp.LAST_LON);
        p.add(lon1.getTitle());
        p.add(lon1.getTextBox());

        p.add(lon2.getTitle());
        p.add(lon2.getTextBox());
        return p;
    }

    private JPanel lats() {
        JPanel p = new JPanel();
        this.lat1 = new TitleFieldComponents("First Latitude: ", 7, "degrees", ParameterHelp.FIRST_LAT);
        this.lat2 = new TitleFieldComponents("Last Latitude: ", 7, "degrees", ParameterHelp.LAST_LAT);
        p.add(lat1.getTitle());
        p.add(lat1.getTextBox());

        p.add(lat2.getTitle());
        p.add(lat2.getTextBox());
        return p;
    }

	@Override
	public String methodHelp() {
		return MethodHelp.MAP_VALUES_DEPTH.getMethodTip();
	}

	@Override
	public ActionListener getAcceptButtonListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					presenter.mapValuesLayer(
							lat1.getFieldValue(),
							lat2.getFieldValue(),
							latSpacing.getTextField().getText(),
							lon1.getFieldValue(),
							lon2.getFieldValue(),
							lonSpacing.getTextField().getText(),
							Integer.parseInt(layerID.getFieldValue()), Double.parseDouble(fractionalRadius.getFieldValue()),
							horizontal.getInterpolation(),
							radial.getInterpolation(),
							reciprocal.getSelected(),
							attributes.getCheckedAttributeIndexes(),
							output.getText());
					destroy();
				} catch (Exception e) {
					setErrorVisible(true);
				}

			}
		};
	}
}
