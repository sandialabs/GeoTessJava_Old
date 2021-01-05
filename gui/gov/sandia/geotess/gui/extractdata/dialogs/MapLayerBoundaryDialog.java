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
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class MapLayerBoundaryDialog extends AbstractModelNeededDialog {

	private LatLonComponents latlon1;
	private LatLonComponents latlon2;
    private DeltaOrNComponents latSpacing;
    private DeltaOrNComponents lonSpacing;
    private HorizontalInterpolationComponents horizontal;
    private TitleFieldComponents layerID;
    private RadioButtonPanel topBottom;
    private RadioButtonPanel depthRadius;
    private FileIOComponents output;

    public MapLayerBoundaryDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Map Layer Boundary Dialog");
    }

    @Override
    public String methodHelp() {
        return MethodHelp.MAP_LAYER_BOUNDARY.getMethodTip();
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
                	
                    presenter.mapLayerBoundaries(lat1, lat2, latSpacing.getTextField().getText(),
                            lon1, lon2, lonSpacing.getTextField().getText(),
                            Integer.parseInt(layerID.getFieldValue()), topBottom.getSelected(), depthRadius.getSelected(),
                            horizontal.getInterpolation(), output.getText());

                    destroy();
                } catch(IOException | GeoTessException | NumberFormatException ee) {
                	setErrorVisible(true);
                }
            }
        };
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill", "[][]15[]"));

        this.output = new FileIOComponents("Output File: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.latSpacing = new DeltaOrNComponents("Latitude Spacing: ");
        this.lonSpacing = new DeltaOrNComponents("Longitude Spacing: ");
        this.topBottom = new RadioButtonPanel("Top", "Bottom");
        this.depthRadius = new RadioButtonPanel("Depth", "Radius");
        this.horizontal = new HorizontalInterpolationComponents();
        this.latlon1 = new LatLonComponents("First Latitude: ", "First Longitude: ");
        this.latlon2 = new LatLonComponents("Last Latitude: ", "Last Longitude: ");
               
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
 
        
        TitleFieldComponents components = new TitleFieldComponents("Layer ID: ", 4, ParameterHelp.LAYER_ID);
        panel.add(components.getTitle());
        panel.add(components.getTextBox(), "wrap");
        
        panel.add(new PopupLabel("Top Or Bottom of Layer: ", ParameterHelp.TOP_OR_BOTTOM));
        panel.add(topBottom, "wrap");
        
        panel.add(new PopupLabel("Depth Or Radius: ", ParameterHelp.DEPTH_OR_RADIUS));
        panel.add(depthRadius, "wrap");
      
        panel.add(horizontal.getTitle());
        panel.add(horizontal.getButtons(), "wrap");

        panel.add(output.getTitle());
        panel.add(output.getTextBox());
        panel.add(output.getButton(), "wrap");
       
        return panel;
    }
}

