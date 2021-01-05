package gov.sandia.geotess.gui.utilities.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeepestShallowestComponents;
import gov.sandia.geotess.gui.tools.ErrorLabel;
import gov.sandia.geotess.gui.tools.LatLonComponents;
import gov.sandia.geotess.gui.tools.PopupLabel;
import gov.sandia.geotess.gui.tools.RadioButtonPanel;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ProfileDialog extends AbstractModelNeededDialog {

    private LatLonComponents latlon;
    private DeepestShallowestComponents layers;
    private RadioButtonPanel depthRadius;
    private ReciprocalComponents reciprocal;
    private AttributeCheckboxPanel attributes;
    
    public ProfileDialog(GeoTessPresenter presenter, JFrame parent) {
        super(presenter, parent, "Profile Dialog");
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new SubmitButtonListener();
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout());

        this.depthRadius = new RadioButtonPanel("Depth", "Radius");
        this.reciprocal = new ReciprocalComponents();
        this.layers = new DeepestShallowestComponents(model);
        this.latlon = new LatLonComponents();
        this.attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(300, 150));
        
        p.add(latlon.getLatTitle());
        p.add(latlon.getLatTextBox(), "split 2");
        p.add(latlon.getLatUnits(), "wrap");
        
        p.add(latlon.getLonTitle());
        p.add(latlon.getLonTextBox(), "split 2");
        p.add(latlon.getLonUnits(), "wrap");
        
        //deepest shallowest
        p.add(layers.getDeepLabel());
        p.add(layers.getDeepBox(), "wrap");
        p.add(layers.getShallowLabel());
        p.add(layers.getShallowBox(), "wrap");

        //depth or radius
        p.add(new PopupLabel("Depth Or Radius: ", ParameterHelp.DEPTH_OR_RADIUS));
        p.add(depthRadius, "wrap");

        //reciprocal
        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "wrap");

        p.add(attributes, "spanx 3, wrap");
        
        return p;
    }

    @Override
    public String methodHelp() {
        return MethodHelp.PROFILE.getMethodTip();
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            int[] attributeIndexes = attributes.getCheckedAttributeIndexes();
            try {
                presenter.profile(latlon.getLat(), latlon.getLon(), layers.getDeepestIndex(),
                        layers.getShallowestIndex()
                        , depthRadius.getSelected(), reciprocal.getSelected(), attributeIndexes);
                destroy();
            } catch(IOException | GeoTessException | NumberFormatException e) {
            	setErrorVisible(true);
            }
        }
    }



}
