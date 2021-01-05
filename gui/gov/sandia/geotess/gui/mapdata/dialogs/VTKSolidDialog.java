package gov.sandia.geotess.gui.mapdata.dialogs;

import gov.sandia.geotess.GeoTessException;
import gov.sandia.geotess.GeoTessModel;
import gov.sandia.geotess.gui.enums.FileOperation;
import gov.sandia.geotess.gui.enums.MethodHelp;
import gov.sandia.geotess.gui.enums.ParameterHelp;
import gov.sandia.geotess.gui.mainlayout.GeoTessPresenter;
import gov.sandia.geotess.gui.tools.AbstractModelNeededDialog;
import gov.sandia.geotess.gui.tools.AttributeCheckboxPanel;
import gov.sandia.geotess.gui.tools.DeepestShallowestComponents;
import gov.sandia.geotess.gui.tools.FileIOComponents;
import gov.sandia.geotess.gui.tools.HorizontalInterpolationComponents;
import gov.sandia.geotess.gui.tools.RadialInterpolationComponents;
import gov.sandia.geotess.gui.tools.ReciprocalComponents;
import gov.sandia.geotess.gui.tools.TitleFieldComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Created by dmdaily on 7/29/2014.
 */
public class VTKSolidDialog extends AbstractModelNeededDialog {

    private FileIOComponents output;
    private TitleFieldComponents rspacing;
    private DeepestShallowestComponents layers;
    private HorizontalInterpolationComponents horizontalInterpolation;
    private RadialInterpolationComponents radialInterpolation;
    private ReciprocalComponents reciprocal;
    private AttributeCheckboxPanel attributes;
    
    public VTKSolidDialog(GeoTessPresenter presenter, JFrame parent, String title) {
        super(presenter, parent, title);
    }

    @Override
    public JPanel makeNewDialog(GeoTessModel model) {
        JPanel p = new JPanel();
        p.setLayout(new MigLayout("fill"));
        this.output = new FileIOComponents("Output: ", FileOperation.SAVE, this, ParameterHelp.OUTPUT);
        this.rspacing = new TitleFieldComponents("Max Radial Spacing: ", 3, "km", ParameterHelp.MAX_RADIAL_SPACING);
        this.layers = new DeepestShallowestComponents(model);
        this.horizontalInterpolation = new HorizontalInterpolationComponents();
        this.radialInterpolation = new RadialInterpolationComponents();
        this.reciprocal = new ReciprocalComponents();
        this.attributes = new AttributeCheckboxPanel(model);
        this.attributes.setPreferredSize(new Dimension(600,150));
        p.add(output.getTitle());
        p.add(output.getTextBox());
        p.add(output.getButton(), "wrap");

        p.add(rspacing.getTitle());
        p.add(rspacing.getTextBox(), "wrap");

        p.add(layers.getDeepLabel());
        p.add(layers.getDeepBox(), "wrap");

        p.add(layers.getShallowLabel());
        p.add(layers.getShallowBox(), "wrap");

        p.add(horizontalInterpolation.getTitle());
        p.add(horizontalInterpolation.getButtons(), "wrap");

        p.add(radialInterpolation.getTitle());
        p.add(radialInterpolation.getButtons(), "wrap");

        p.add(reciprocal.getTitle());
        p.add(reciprocal.getButtons(), "wrap");
        
        p.add(attributes, "span 3, wrap");
        return p;
    }

    @Override
    public ActionListener getAcceptButtonListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    presenter.vtkSolid(output.getText(), Double.parseDouble(rspacing.getFieldValue()), layers.getDeepestIndex(),
                            layers.getShallowestIndex(), horizontalInterpolation.getInterpolation(),
                            radialInterpolation.getInterpolation(), reciprocal.getSelected(),
                            attributes.getCheckedAttributeIndexes());
                    destroy();
                } catch(IOException | GeoTessException | NumberFormatException ee) {
                	setErrorVisible(true);
                }
            }
        };
    }

    @Override
    public String methodHelp() {
        return MethodHelp.VTK_SOLID.getMethodTip();
    }

}
